package com.patrickluhmann.plobjectslibrary;

import android.graphics.Canvas;
import android.graphics.Color;

public class PLObject {
	public static String getClassName() { return "PLObject"; }
	private int sizeX;
	private int sizeY;
	private float posX;
	private float posY;
	private float velX;
	private float velY;
	private int color;

	public static abstract class AbsPLOBuilder<C extends PLObject, B extends AbsPLOBuilder<C, B>> {
		int sizeX = 0;
		int sizeY = 0;
		private float posX = 0;
		private float posY = 0;
		private float velX = 0;
		private float velY = 0;
		private int color = Color.WHITE;

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
		public B color(int val) {
			this.color = val;
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
		color = builder.color;
	}

	public void getSize(int[] pos) {
		pos[0] = sizeX;
		pos[1] = sizeY;
	}

	public void getPosition(float[] pos) {
		pos[0] = posX;
		pos[1] = posY;
	}

	public void getVelocity(float[] pos) {
		pos[0] = velX;
		pos[1] = velY;
	}

	public int getColor() {
		return color;
	}

	public void draw(Canvas canvas) {

	}

	public void updatePosition(float deltaT) {
		posX += (velX * deltaT);
		posY += (velY * deltaT);
	}
}
