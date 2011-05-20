package net.sabamiso.android.test.multitouchtest;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

class FingerPoint {
	public int id;
	public float x;
	public float y;
	public float pressure;

	public final int[] color = { Color.RED, Color.GREEN, Color.BLUE,
			Color.YELLOW, Color.MAGENTA, Color.CYAN };

	public FingerPoint() {
	};

	public FingerPoint(MotionEvent evt, int idx) {
		this.id = evt.getPointerId(idx);
		this.x = evt.getX(idx);
		this.y = evt.getY(idx);
		this.pressure = evt.getPressure(idx);
	}

	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(color[id % color.length]);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2.0f);

		canvas.drawCircle(x, y, 50.0f, paint);
		canvas.drawLine(0, y, canvas.getWidth() - 1, y, paint);
		canvas.drawLine(x, 0, x, canvas.getHeight() - 1, paint);
	}

	public void drawStatusText(Canvas canvas, int x, int y, Paint paint) {
		String msg = "";
		msg += "id " + id;
		msg += " : ";
		msg += "[x:" + this.x + ", ";
		msg += "y:" + this.y + ", ";
		msg += "pressure:" + this.pressure + "]";
		
		canvas.drawText(msg, x, y, paint);
	}
}

class FingerPointContainer extends Vector<FingerPoint> {
	private static final long serialVersionUID = 1L;

	public FingerPointContainer() {
		super();
	}

	public void setFingerPoint(MotionEvent evt) {
		clear();
		for (int i = 0; i < evt.getPointerCount(); ++i) {
			this.add(new FingerPoint(evt, i));
		}
	}

	public void removeFingerPoint(int idx) {
		remove(idx);
	}

	public void draw(Canvas canvas) {
		for (FingerPoint p : this) {
			p.draw(canvas);
		}

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(16);
		paint.setFakeBoldText(true);
		paint.setColor(Color.BLACK);
		for (int i = 0; i < size(); ++i) {
			int x = 10;
			int y = 30 + i * 20;
			get(i).drawStatusText(canvas, x, y, paint);
		}
	}
}

public class MultiTouchView extends View {
	FingerPointContainer container = new FingerPointContainer();

	public MultiTouchView(Context context) {
		super(context);
		container.clear();
	}

	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		int action = evt.getAction() & MotionEvent.ACTION_MASK;
		int pointer_idx = (evt.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

		switch (action) {
		case MotionEvent.ACTION_DOWN: // 1�{�̎w���G�ꂽ�ꍇ
		case MotionEvent.ACTION_POINTER_DOWN: // ���ł�1�{�ȏ�w���G��Ă����ԂŁA����Ɏw���ӂꂽ�ꍇ
		case MotionEvent.ACTION_MOVE:
			container.setFingerPoint(evt);
			break;
		case MotionEvent.ACTION_UP: // �Ō��1�{�̎w�𗣂����ꍇ
			container.clear();
			break;
		case MotionEvent.ACTION_POINTER_UP: // 2�{�ȏ�w���G��Ă����ԂŁA1�{�w�𗣂����ꍇ
			container.setFingerPoint(evt);
			
			// ���ۂɂ�ACTION_POINTER_?_UP���������Ă���̂ŁA
			// �w�������ꂽ�|�C���g��index���擾���č폜����
			container.removeFingerPoint(pointer_idx);
			break;
		}

		// redraw
		invalidate();

		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		setBackgroundColor(Color.WHITE);

		container.draw(canvas);
	}
}
