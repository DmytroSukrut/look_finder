package com.look_finder.service;

import com.look_finder.position.PositionEntity;
import com.look_finder.position.PositionRepository;
import com.look_finder.user.UserInformationEntity;
import com.look_finder.user.UserInformationRepository;
import com.look_finder.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimilarService {

    private final UserRepository userRepository;
    private final PositionRepository positionRepository;
    private final UserInformationRepository userInformationRepository;

    public SimilarService(UserRepository userRepository, PositionRepository positionRepository, UserInformationRepository userInformationRepository) {
        this.userRepository = userRepository;
        this.positionRepository = positionRepository;
        this.userInformationRepository = userInformationRepository;
    }

    public List<Map<String, Object>> getSimilar(long id){

        UserInformationEntity userInformationEntity = userInformationRepository.findByUserId(id);
        String queryEmbedding = userInformationEntity.getFavourite();

        int limit = 24;

        List<Map<String, Object>> similar_positions = new ArrayList<>();

        positionRepository.findSimilarProducts(queryEmbedding, "36", "f")
                .stream()
                .limit(limit)
                .map(this::from_entity_to_map)
                .forEach(similar_positions::add);


        System.out.println("similar_positions:\n" + similar_positions);

        return similar_positions;
    }

    private Map<String, Object> from_entity_to_map(PositionEntity entity) {
        Map<String, Object> position = new HashMap<>();

        position.put("id", entity.getId());
        position.put("origin", entity.getOrigin());
        position.put("name", entity.getName());

        if(!Objects.equals(entity.getNameEn(), "not found")) {
            position.put("name_en", entity.getNameEn());
        }

        position.put("position_color", entity.getPositionsColor());
        position.put("size", entity.getSize());
        position.put("price", entity.getPrice());

        List<Map<String, Object>> photos = new ArrayList<>();
        Map<String,Object> photo = new HashMap<>();

        photo.put("display", entity.getDisplay());
        photos.add(photo);

        if(!Objects.equals(entity.getPhoto0(), "not found")) {
            photo = new HashMap<>();
            photo.put("0", entity.getPhoto0());
            photos.add(photo);
        }
        if(!Objects.equals(entity.getPhoto1(), "not found")) {
            photo = new HashMap<>();
            photo.put("1", entity.getPhoto1());
            photos.add(photo);
        }
        if(!Objects.equals(entity.getPhoto2(), "not found")) {
            photo = new HashMap<>();
            photo.put("2", entity.getPhoto2());
            photos.add(photo);
        }
        if(!Objects.equals(entity.getPhoto3(), "not found")) {
            photo = new HashMap<>();
            photo.put("3", entity.getPhoto3());
            photos.add(photo);
        }

        position.put("photos", photos);

        return position;
    }

}
