package com.general.template.service.implementation;

import com.general.template.common.Constants;
import com.general.template.domain.Score;
import com.general.template.domain.dto.ScoreDTO;
import com.general.template.repository.ScoreRepository;
import com.general.template.service.ScoreService;
import com.general.template.service.mapping.ScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor=@__(@Autowired))
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final ScoreMapper scoreMapper;
    private final RedisService redisService;

    @Override
    public List<ScoreDTO> getLatestScores() {
        List<? extends Object> cachedScores= redisService.getFromListByRange(Constants.LATEST_REDIS_KEY, 0, -1);
        if(cachedScores.size() > 0){
            return scoreMapper.mapResponse((List<Score>) cachedScores);
        }
        List<Score> scores = scoreRepository.findLatestScores();
        return scoreMapper.mapResponse(scores);
    }

    @Override
    public Page<Score> search(String searchParam, int page, int size, String sortBy) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        return scoreRepository.findAllByTitleContainingOrDescriptionContaining(searchParam, searchParam, paging);
    }

    @Override
    public Page<Score> searchV2(String searchParam, int page, int size, String sortBy) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
//        return scoreRepository.findAllByTitleContainingOrDescriptionContaining(searchParam, searchParam, paging);
        return scoreRepository.searchKeyword("*"+searchParam+"*", paging);
    }
}
