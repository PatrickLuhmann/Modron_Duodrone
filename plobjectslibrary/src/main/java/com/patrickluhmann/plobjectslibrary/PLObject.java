package com.patrickluhmann.plobjectslibrary;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.StrictMath.max;

public class PLObject {
	public static String getClassName() { return "PLObject"; }
	// Settable attributes
	private int sizeX;
	private int sizeY;
	private float posX;
	private float posY;
	private float velX;
	private float velY;
	private Bitmap skin;

	private Rect rect;

	public static abstract class AbsPLOBuilder<C extends PLObject, B extends AbsPLOBuilder<C, B>> {
		int sizeX = 0;
		int sizeY = 0;
		private float posX = 0;
		private float posY = 0;
		private float velX = 0;
		private float velY = 0;
		private Bitmap skin;

		@SuppressWarnings("unchecked")
		public B posX(float val) {
			this.posX = val;
			return (B) this;
		}

		@SuppressWarnings("unchecked")
		public B posY(float val) {
			this.posY = val;
			return (B) this;
		}

		@SuppressWarnings("unchecked")
		public B velX(float val) {
			this.velX = val;
			return (B) this;
		}

		@SuppressWarnings("unchecked")
		public B velY(float val) {
			this.velY = val;
			return (B) this;
		}

		@SuppressWarnings("unchecked")
		public B skin(Bitmap val) {
			this.skin = val;
			return (B) this;
		}

		public abstract C build();
	}

	public static class PLOBuilder extends AbsPLOBuilder<PLObject, PLOBuilder> {
		@Override
		public PLObject build() {
			return new PLObject(this);
		}
	}

	// Builder interface
	// Mandatory parameters:
	// - sizeX and sizeY: the dimensions of the object.
	public static AbsPLOBuilder<?, ?> builder(int sizeX, int sizeY) {
		PLOBuilder temp = new PLOBuilder();
		temp.sizeX = sizeX;
		temp.sizeY = sizeY;
		return temp;
	}

	protected PLObject(AbsPLOBuilder<?, ?> builder) {
		sizeX = builder.sizeX;
		sizeY = builder.sizeY;
		posX = builder.posX;
		posY = builder.posY;
		velX = builder.velX;
		velY = builder.velY;
		skin = builder.skin;

		// Remember: "Note that the right and bottom coordinates are exclusive."
		rect = new Rect((int)posX, (int)posY, (int)posX + sizeX, (int)posY + sizeY);
	}

	public void getSize(int[] pos) {
		pos[0] = sizeX;
		pos[1] = sizeY;
	}

	public void getPosition(float[] pos) {
		pos[0] = posX;
		pos[1] = posY;
	}

	public void setPosition(float x, float y) {
		posX = x;
		posY = y;
		rect.offsetTo((int)posX, (int)posY);
	}

	public void getVelocity(float[] pos) {
		pos[0] = velX;
		pos[1] = velY;
	}

	public void setVelocity(float vx, float vy) {
		velX = vx;
		velY = vy;
	}

	public void adjustVelocity(float deltaVX, float deltaVY) {
		velX += deltaVX;
		velY += deltaVY;
	}

	// Modifies the (x, y) coordinates based on applying the current velocity
	// over the given time period.
	public void updatePosition(float deltaT) {
		float newX = posX + (velX * deltaT);
		float newY = posY + (velY * deltaT);
		setPosition(newX, newY);
	}

	public boolean contains(float x, float y) {
		if (rect.contains((int)x, (int)y)) {
			return true;
		}
		return false;
	}

	private Rect getMovementBoundingRectangle(float deltaT) {
		int top1 = rect.top;
		int left1 = rect.left;
		int bottom1 = rect.bottom;
		int right1 = rect.right;
		float newX = posX + (velX * deltaT);
		float newY = posY + (velY * deltaT);
		Rect future = new Rect(rect);
		future.offsetTo((int)newX, (int)newY);
		int top2 = future.top;
		int left2 = future.left;
		int bottom2 = future.bottom;
		int right2 = future.right;

		return new Rect(min(left1, left2),
			min(top1, top2), max(right1, right2), max(bottom1, bottom2));
	}

	/**
	 * Determine if this object will collide with the target object
	 * over the given length of time. If so, return the amount of time
	 * that corresponds to the instant before collision, which the caller
	 * can use when handling the effects of the collision.
	 * @param target The object that might be hit
	 * @param deltaT The length of time to look ahead
	 * @return The length of time to just before the collision; -1 if
	 * there will not be a collision; -2 if the objects already overlap.
	 */
	public float willCollide(PLObject target, float deltaT) {
		// Special Case: If the objects already overlap, exit now.
		if (Rect.intersects(rect, target.rect))
			return -2;

		// Special Case: If neither object is moving, then they obviously will
		// never collide, so exit now.
		if (velX == 0 && velY == 0 && target.velX == 0 && target.velY == 0)
			return -1;

		// Determine the movement bounding rectangle for both objects. If these
		// rectangles do not overlap, then there is no possibility of a collision.

		Rect mbrThis = this.getMovementBoundingRectangle(deltaT);
		Rect mbrTgt = target.getMovementBoundingRectangle(deltaT);

		if (Rect.intersects(mbrThis, mbrTgt)) {
			// There might be a collision.

			// We are going to move the objects one px at a time to get best resolution
			// for collision detection. To do this, we need to determine the fastest
			// velocity. Then we will loop that many times to update the positions.
			// For this purpose, we are interested in the magnitude of the velocity,
			// so we need to ignore the sign.
			int numSlicesThis = max((int)(abs(velX) * deltaT), (int)(abs(velY) * deltaT));
			int numSlicesTgt = max((int)(abs(target.velX) * deltaT), (int)(abs(target.velY) * deltaT));
			int numSlices = max(numSlicesThis, numSlicesTgt);

			float sliceT = deltaT / numSlices;

			// Start where we currently are
			float prevXThis = posX;
			float prevYThis = posY;
			Rect futureThis = new Rect(rect);
			float prevXTgt = target.posX;
			float prevYTgt = target.posY;
			Rect futureTgt = new Rect(target.rect);
			for (int s = 0; s < numSlices; s++) {
				// Calc where this object will be after one slice
				float newX = prevXThis + (velX * sliceT);
				float newY = prevYThis + (velY * sliceT);
				futureThis.offsetTo((int)newX, (int)newY);
				prevXThis = newX;
				prevYThis = newY;
				// Calc where target object will be after one slice
				newX = prevXTgt + (target.velX * sliceT);
				newY = prevYTgt + (target.velY * sliceT);
				futureTgt.offsetTo((int)newX, (int)newY);
				prevXTgt = newX;
				prevYTgt = newY;

				// See if the two objects intersect
				if (Rect.intersects(futureThis, futureTgt)) {
					return (s + 0) * sliceT;
				}
			}
		}

		return -1;
	}

	public boolean overlaps(PLObject obj) {
		return rect.intersect(obj.rect);
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(skin, null, rect, null);
	}
}
