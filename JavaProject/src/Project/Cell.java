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
            cell.add(new ArrayList<Boolean>()); //셀 테이블 생성
            for (int j = 0; j < ori_width; ++j)
            {
                cell.get(i).add(false);
            }
        }
	}
	
	public void CreateChecklist(int h,int w) //체크리스트 생성 --> 전체 배열을 검사하지 않고 체크리스트에 등록된 셀과 셀주변만 검사하기위해
    {
        OutofBoundsCheck(h, w); //선택된 셀 + 선텍된 셀 주변 셀 총 9개
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
        boolean outOfbounds = x < 0 || x >= ori_width || y < 0 || y >= ori_height; //창 크기를 벗어나는지 검사
        if (!outOfbounds) //창을 벗어나지 않으면
        {
            if (!checklist.containsKey(p)) //현재 입력된 위치 (키값)이 이미 존재하는지 검사 --> 중복 금지
                checklist.put(p, new NextCellIndex(y, x, true)); //등록된 키값의 value가 없으면 Dictionary에 현재 위치 Point 를 키값으로 셀 등록
        }
	}
	
	public void AliveCell()
	{
		int totalAlive = 0;
		for(Point key:checklist.keySet())
		{
			int h = checklist.get(key).Array_1(), w = checklist.get(key).Array_2();
			totalAlive = NeighborCell(w, h, -1, 0)  //지금 셀 기준으로 주변셀의 생존 개수 얻어오기
                    + NeighborCell(w, h, -1, 1)
                    + NeighborCell(w, h, 0, 1)
                    + NeighborCell(w, h, 1, 1)
                    + NeighborCell(w, h, 1, 0)
                    + NeighborCell(w, h, 1, -1)
                    + NeighborCell(w, h, 0, -1)
                    + NeighborCell(w, h, -1, -1);
			
			if (cell.get(h).get(w) && (totalAlive == 2 || totalAlive == 3)) //주변 셀 개수의 따라 다음세대에 살아남을지 죽을지 결정(Life Game 핵심 알고리즘)
            {
                nextcell.add(new NextCellIndex(h, w, true)); //변경 예정인 셀들의 목록인 nextcell에 등록   
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
	int NeighborCell(int x, int y, int offset_x, int offset_y) //이웃 셀 검사 함수
    {
        int proposeX = x + offset_x; //현재 위치값 + offset값 으로 검사할 위치값 얻기
        int proposeY = y + offset_y;

        boolean outOfbounds = proposeX < 0 || proposeX >= ori_width || proposeY < 0 || proposeY >= ori_height; //창 크기를 벗어나는 위치는 검사 x
        if (!outOfbounds)
            return cell.get(proposeY).get(proposeX) ? 1 : 0; ; //검사할 위치의 셀에 상태에따라 살아있으면 1 죽어있으면 0 리턴
        return 0;
    }

}
