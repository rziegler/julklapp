package ch.bbv.julklapp.persistence;

import java.util.List;

import ch.bbv.julklapp.dto.CircleDto;
import ch.bbv.julklapp.dto.CredentialsDto;
import ch.bbv.julklapp.dto.MemberDto;
import ch.bbv.julklapp.dto.WichteliDto;

public interface PersistenceFacade {

	CircleDto getCircleDtoByName(String circleName);

	MemberDto getMemberDtoInCircleByName(String circleName, String memberName);

	CircleDto createCircle(CircleDto circleDto);

	MemberDto createMember(String circleName, MemberDto memberDto);

	List<CircleDto> getCircleDtos();

	CircleDto deleteCircleDto(String name);

	void shuffle(String name);

	void notify(String name);

	WichteliDto getWichteli(String name, String memberName, CredentialsDto value);
}