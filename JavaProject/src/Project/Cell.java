package Project;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cell {
	
	public List<List<Boolean>> cell;
	private List<NextCellIndex> nextcell;
	public Map<Point, NextCellIndex> checklist;
	public int ori_width;
	public int ori_height;
	Cell(int w,int h)
	{
		cell = new ArrayList<List<Boolean>>();
		nextcell = new ArrayList<NextCellIndex>();
		checklist = new HashMap<Point,NextCellIndex>();
		ori_height = h;
		ori_width = w;
		
		for (int i = 0; i < ori_height; ++i)
        {
            cell.add(new ArrayList<Boolean>()); //�� ���̺� ����
            for (int j = 0; j < ori_width; ++j)
            {
                cell.get(i).add(false);
            }
        }
	}
	
	public void CreateChecklist(int h,int w) //üũ����Ʈ ���� --> ��ü �迭�� �˻����� �ʰ� üũ����Ʈ�� ��ϵ� ���� ���ֺ��� �˻��ϱ�����
    {
        OutofBoundsCheck(h, w); //���õ� �� + ���ص� �� �ֺ� �� �� 9��
        OutofBoundsCheck(h, w-1);
        OutofBoundsCheck(h + 1, w-1);
        OutofBoundsCheck(h+1, w);
        OutofBoundsCheck(h+1, w+1);
        OutofBoundsCheck(h, w+1);
        OutofBoundsCheck(h-1, w+1);
        OutofBoundsCheck(h-1, w);
        OutofBoundsCheck(h-1, w-1);
    }
	
	private void OutofBoundsCheck(int y,int x)
	{
		Point p = new Point(x, y);
        boolean outOfbounds = x < 0 || x >= ori_width || y < 0 || y >= ori_height; //â ũ�⸦ ������� �˻�
        if (!outOfbounds) //â�� ����� ������
        {
            if (!checklist.containsKey(p)) //���� �Էµ� ��ġ (Ű��)�� �̹� �����ϴ��� �˻� --> �ߺ� ����
                checklist.put(p, new NextCellIndex(y, x, true)); //��ϵ� Ű���� value�� ������ Dictionary�� ���� ��ġ Point �� Ű������ �� ���
        }
	}
	
	public void AliveCell()
	{
		int totalAlive = 0;
		for(Point key:checklist.keySet())
		{
			int h = checklist.get(key).Array_1(), w = checklist.get(key).Array_2();
			totalAlive = NeighborCell(w, h, -1, 0)  //���� �� �������� �ֺ����� ���� ���� ������
                    + NeighborCell(w, h, -1, 1)
                    + NeighborCell(w, h, 0, 1)
                    + NeighborCell(w, h, 1, 1)
                    + NeighborCell(w, h, 1, 0)
                    + NeighborCell(w, h, 1, -1)
                    + NeighborCell(w, h, 0, -1)
                    + NeighborCell(w, h, -1, -1);
			
			if (cell.get(h).get(w) && (totalAlive == 2 || totalAlive == 3)) //�ֺ� �� ������ ���� �������뿡 ��Ƴ����� ������ ����(Life Game �ٽ� �˰���)
            {
                nextcell.add(new NextCellIndex(h, w, true)); //���� ������ ������ ����� nextcell�� ���   
            }
            else if (!cell.get(h).get(w) && totalAlive == 3)
            {
                nextcell.add(new NextCellIndex(h, w, true));
            }
            else
            {
                nextcell.add(new NextCellIndex(h, w, false));
            }
		}
		
		for(NextCellIndex item : nextcell)
		{
			cell.get(item.Array_1()).set(item.Array_2(),item.Life());
			if(item.Life())
				CreateChecklist(item.Array_1(), item.Array_2());
		}
		nextcell.clear();
	}
	int NeighborCell(int x, int y, int offset_x, int offset_y) //�̿� �� �˻� �Լ�
    {
        int proposeX = x + offset_x; //���� ��ġ�� + offset�� ���� �˻��� ��ġ�� ���
        int proposeY = y + offset_y;

        boolean outOfbounds = proposeX < 0 || proposeX >= ori_width || proposeY < 0 || proposeY >= ori_height; //â ũ�⸦ ����� ��ġ�� �˻� x
        if (!outOfbounds)
            return cell.get(proposeY).get(proposeX) ? 1 : 0; ; //�˻��� ��ġ�� ���� ���¿����� ��������� 1 �׾������� 0 ����
        return 0;
    }

}
