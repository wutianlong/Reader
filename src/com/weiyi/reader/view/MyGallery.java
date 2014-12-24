package com.weiyi.reader.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

import com.weiyi.reader.util.ViewUtil;

/**
 * �Զ���GalleryЧ����չʾ����С˵�б�
 * 
 * @author κ����
 * @version 1.0
 * */
public class MyGallery extends Gallery {
	private Camera camera = new Camera();
	private int maxRotationAngle = 50;// �����ת�Ƕȣ���3D�л�Ч��
	private int maxZoom = -200;// �������ֵ,�������Ŵ�ͼƬ������ͼƬ��

	public MyGallery(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);// TODO Auto-generated
													// constructor stub
	}

	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);// TODO Auto-generated
													// constructor stub
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);// TODO Auto-generated
													// constructor stub
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		int childCenter = ViewUtil.getCenterOfView(child);
		int ratationAngle = 0;

		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
		if (childCenter == getCenterOfGallery()) {// �����View��Gallery��������ת�����л���
			transformWithCamera(child, t, 0);
		} else {
			ratationAngle = ((getCenterOfGallery() - ViewUtil
					.getCenterOfView(child)) / child.getWidth())
					* maxRotationAngle;// �ؼ�֮һ
			if (Math.abs(ratationAngle) > maxRotationAngle) {
				ratationAngle = (ratationAngle < 0) ? -maxRotationAngle
						: maxRotationAngle;
			}
			transformWithCamera(child, t, ratationAngle);
		}
		return true;
	}

	private void transformWithCamera(View child, Transformation t,
			int rotationAngle) {
		camera.save();
		Matrix matrix = t.getMatrix();
		int imgWidth = child.getLayoutParams().width;
		int imgHeight = child.getLayoutParams().height;
		int rotation = Math.abs(rotationAngle);

		camera.translate(0.0f, 0.0f, 100.0f);
		if (rotation < maxRotationAngle) {
			float zoomAmount = (float) (maxZoom + (rotation * 1.5));
			camera.translate(0.0f, 0.0f, zoomAmount);
		}
		camera.rotateY(rotationAngle);
		camera.getMatrix(matrix);
		matrix.preTranslate(-(imgWidth / 2), -(imgHeight / 2));
		matrix.postTranslate((imgWidth / 2), (imgHeight / 2));
		camera.restore();
	}

	public int getCenterOfGallery() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	public int getMaxRotationAngle() {
		return maxRotationAngle;
	}

	public void setMaxRotationAngle(int maxRotationAngle) {
		this.maxRotationAngle = maxRotationAngle;
	}

	public int getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		this.maxZoom = maxZoom;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (velocityX > 0) {
			super.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
		} else {
			super.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		}
		return false;
	}
}
