package ETS.Timetable;

import java.util.Vector;

 public class Matrix3D<T> {
	private int P;//Page
	private int R;//Row
	private int C;//Column
	private Vector<T> Content;
	private T temp;

	public Matrix3D(int P, int R, int C) {
		this.P = P;
		this.R = R;
		this.C = C;
		Content = new Vector<T>();
		Content.setSize(P * R * C);
	}

	public T getT(){
		return temp;
	}
	public int getP(){
		return P;
	}
	public int getR(){
		return R;
	}
	public int getC(){
		return C;
	}
	public void clearContent() {
		Content.clear();
	}

	public Vector<T> getContent() {
		return Content;
	}

	public T getContent(int i, int j, int k) {
		// A[l][m][n];
		// A[i][j][k] -> (((i*m)+j)*n)+k
		return Content.get((((i * R) + j) * C) + k);
	}
	
	public void print() {
		// A[l][m][n];
		// A[i][j][k] -> (((i*m)+j)*n)+k
		for(int i=0;i<P;i++){
		 System.out.println("\n Page:"+(i+1));
		 for(int j=0;j<R;j++){
		  for(int k=0;k<C;k++)
		   System.out.print(Content.get((((i * R) + j) * C) + k)+"\t");
		  System.out.print("\n");
		 }
		}
	}

	public void setContent(int i, int j, int k, T t) {
		// A[l][m][n];
		// A[i][j][k] -> (((i*m)+j)*n)+k
		Content.set((((i * R) + j) * C) + k,t);
	}
	
	public void Init(T t){
		this.temp=t;
		clearContent();
		for(int i=0;i<P*R*C;i++)
			getContent().add(i, t);
	}
	
	public Matrix2D<T> get2DPart(int i){
		Matrix2D<T> M2DForm;
		M2DForm = new Matrix2D<T>(R,C) ;
		M2DForm.Init(getT());
		for(int j=0;j<R;j++){
			for(int k=0;k<C;k++)
				M2DForm.setContent(j, k, getContent(i,j,k));
		}
		return M2DForm;
	}
	
	public void set2DPart(int i,Matrix2D<T> M2DForm){
		for(int j=0;j<M2DForm.getR();j++)
			for(int k=0;k<M2DForm.getC();k++)
				setContent(i,j,k,M2DForm.getContent(j, k));
	}
}

