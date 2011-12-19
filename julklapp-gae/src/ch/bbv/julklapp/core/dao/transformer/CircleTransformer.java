package ch.bbv.julklapp.core.dao.transformer;

import ch.bbv.julklapp.core.dao.Circle;
import ch.bbv.julklapp.core.dao.Member;
import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.MemberDto;

public class CircleTransformer {

	private MemberTransformer memberTransformer;
	
	public CircleTransformer() {
		memberTransformer = new MemberTransformer();
	}
	
	public Circle marshal(CircleDto circleDto) {
		Circle circle = new Circle();
		circle.setName(circleDto.getName());
		for (MemberDto memberDto : circleDto.getMembers()) {
			Member member = memberTransformer.marshal(memberDto);
			circle.addMember(member);
		}
		return circle;
	}
	
	public CircleDto unmarshal(Circle circle) {
		CircleDto circleDto = new CircleDto();
		circleDto.setName(circle.getName());
		for (Member member : circle.getMembers()) {
			MemberDto memberDto = memberTransformer.unmarshal(member);
			circleDto.addMember(memberDto);
		}
		return circleDto;
	}
}
