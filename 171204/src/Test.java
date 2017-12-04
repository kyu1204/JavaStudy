
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr1 = {9,20,28,18,11};
		int[] arr2 = {30,1,21,17,28};
		String[] map = new String[5];
		map = decMap(5, arr1, arr2);
		
		for (String item : map) {
			System.out.println(item);
		}
	}

	public static String[] decMap(int n, int[] arr1, int[] arr2)
	{
		String[] resultMap = new String[n];
		String decoder = null;
		int[] tmp = new int[n];
		
		for(int i=0;i<n;++i)
			tmp[i] = arr1[i] | arr2[i];

		for(int i=0;i<n;++i)
		{
			for(int j=0;j<n;++j)
			{
				int intMap = tmp[i];
				if(decoder == null)
					decoder = (((intMap & (1<<j))>>j) == 1) ? "#":" ";
				else
					decoder += (((intMap & (1<<j))>>j) == 1) ? "#":" ";
			}
			decoder = new StringBuffer(decoder).reverse().toString();
			resultMap[i] = decoder;
			decoder = null;			
		}
		return resultMap;
	}
}
