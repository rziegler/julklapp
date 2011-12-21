package ch.bbv.julklapp.dto;

import java.util.ArrayList;

public class CirclesDto {
	
	
	private ArrayList<CircleDto> circleDto;
	
	public CirclesDto() {
		circleDto = new ArrayList<CircleDto>();
	}
	
	
	
	public ArrayList<CircleDto> getCircleDto() {
		return circleDto;
	}
	
	public void setCircleDto( ArrayList<CircleDto> data){
		this.circleDto = data;
	}
	
	
}
