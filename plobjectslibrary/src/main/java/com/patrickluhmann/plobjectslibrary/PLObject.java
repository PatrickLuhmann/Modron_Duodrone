package com.patrickluhmann.plobjectslibrary;

import android.graphics.Canvas;

public class PLObject {
	public static String getClassName() { return "PLObject"; }
	private float posX;
	private float posY;
	private float velX;
	private float velY;

	public static abstract class AbsPLOBuilder<C extends PLObject, B extends AbsPLOBuilder<C, B>> {
		private float posX = 0;
		private float posY = 0;
		private float velX = 0;
		private float velY = 0;

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

		public abstract C build();
	}

	public static class PLOBuilder extends AbsPLOBuilder<PLObject, PLOBuilder> {
		@Override
		public PLObject build() {
			return new PLObject(this);
		}
	}

	public static AbsPLOBuilder<?, ?> builder() {
		return new PLOBuilder();
	}

	protected PLObject(AbsPLOBuilder<?, ?> builder) {
		posX = builder.posX;
		posY = builder.posY;
		velX = builder.velX;
		velY = builder.velY;
	}

	public void draw(Canvas canvas) {

	}

	public void updatePosition(float deltaT) {
		// If this object has a velocity, update its position.
	}
}
