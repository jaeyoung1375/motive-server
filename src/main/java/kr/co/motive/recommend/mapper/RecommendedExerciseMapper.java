package kr.co.motive.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.motive.recommend.dto.RecommendedExerciseResponseDto;

@Mapper
public interface RecommendedExerciseMapper {

	/**
	 * 추천 운동 풀 중 무작위 [count]개를 노출 순서(SORT_NO)로 정렬해 조회
	 * @param count
	 * @return
	 */
	List<RecommendedExerciseResponseDto> getRandomRecommendedExercises(@Param("count") int count);

}
