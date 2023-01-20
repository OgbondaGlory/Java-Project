package ETS.Timetable;

import java.util.Vector;

public class Matrix2D<T> {
	private int R;
	private int C;
	private Vector<T> Content;

	public Matrix2D(int R, int C) {
		this.R = R;
		this.C = C;
		Content = new Vector<T>();
		Content.setSize(R * C);
	}

	public int getR() {
		return R;
	}

	public int getC() {
		return C;
	}

	public void clearContent() {
		Content.clear();
	}

	public Vector<T> getContent() {
		return Content;
	}

	public T getContent(int i, int j) {
		// A[m][n];
		// A[i][j] -> (i*n)+j
		// System.out.println( "["+ String.valueOf(i)+ Content.get((i * C) + j)
		// + String.valueOf(j)+"]");
		return Content.get((i * C) + j);
	}

	public void setContent(int i, int j, T t) {
		// A[l][m][n];
		// A[i][j][k] -> (((i*m)+j)*n)+k
		Content.set((i * C) + j, t);
	}

	public void Init(T t) {
		clearContent();
		for (int i = 0; i < R * C; i++)
			getContent().add(i, t);
	}

	public void print() {
		 for (int j = 0; j < C; j++){
			for (int i = 0; i < R; i++)
				System.out.print(Content.get((i * C) + j) + "{//\\\\}");
			System.out.print("\n");
		}

	}
}
