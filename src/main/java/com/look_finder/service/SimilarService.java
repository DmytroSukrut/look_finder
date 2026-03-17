package com.look_finder.service;

import com.look_finder.components.ShuffleAndDivideComponent;
import com.look_finder.position.PositionEntity;
import com.look_finder.position.PositionRepository;
import com.look_finder.user.UserInformationEntity;
import com.look_finder.user.UserInformationRepository;
import com.look_finder.user.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;

@Service
public class SimilarService {

    private final UserRepository userRepository;
    private final ShuffleAndDivideComponent shuffleAndDivideComponent;
    private final PositionRepository positionRepository;
    private final UserInformationRepository userInformationRepository;

    public SimilarService(UserRepository userRepository, ShuffleAndDivideComponent shuffleAndDivideComponent, PositionRepository positionRepository, UserInformationRepository userInformationRepository) {
        this.userRepository = userRepository;
        this.shuffleAndDivideComponent = shuffleAndDivideComponent;
        this.positionRepository = positionRepository;
        this.userInformationRepository = userInformationRepository;
    }

    public Object getSimilar(long id) throws IOException {
        List<String> ids = new ArrayList<>();
        ids.add("0");
        List<List<Map<String, Object>>> list_of_all = new ArrayList<>();
        UserInformationEntity userInformationEntity = userInformationRepository.findByUserId(id);
        List<Map<String, Object>> key_list = new ArrayList<>();

        int count = count_query(userInformationEntity);
        System.out.println("count: " + count);

        for(int i = 0; i < count; i++) {

            List<Map<String, Object>> similar_positions = new ArrayList<>();
            Map<String, Object> key = new HashMap<>();
            String queryEmbedding = null;

            String size = null;

            switch (i)  {
                case 0 -> {
                    queryEmbedding = userInformationEntity.getFavourite1();
                    key.put("fav1", userInformationEntity.getFavourite1TextId());
                    size = "36";
                }
                case 1 -> {
                    queryEmbedding = userInformationEntity.getFavourite2();
                    key.put("fav2", userInformationEntity.getFavourite2TextId());
                    size = "XXS";
                }
                case 2 -> {
                    queryEmbedding = userInformationEntity.getFavourite3();
                    key.put("fav3", userInformationEntity.getFavourite3TextId());
                }
                case 3 -> {
                    queryEmbedding = userInformationEntity.getFavourite4();
                    key.put("fav4", userInformationEntity.getFavourite4TextId());
                }
                case 4 -> {
                    queryEmbedding = userInformationEntity.getFavourite5();
                    key.put("fav5", userInformationEntity.getFavourite5TextId());
                }
            }
            key_list.add(key);

            positionRepository.findSimilarProducts(queryEmbedding, size, "f", ids.toArray(new String[0]))
                    .stream()
                    .map(this::from_entity_to_map)
                    .forEach(position -> {
                        System.out.println("position: " + get_name(position));
                        ids.add(get_id(position));
                        similar_positions.add(position);
                    });

            list_of_all.add(similar_positions);
        }

//        System.out.println("similar_positions: " + list_of_all);

        return shuffleAndDivideComponent.shuffle_and_divide(list_of_all, "similar", key_list);
    }

    private int count_query(UserInformationEntity userInformationEntity){
        int count = 0;
        if(userInformationEntity.getFavourite1() != null) count++;
        if(userInformationEntity.getFavourite2() != null) count++;
        if(userInformationEntity.getFavourite3() != null) count++;
        if(userInformationEntity.getFavourite4() != null) count++;
        if(userInformationEntity.getFavourite5() != null) count++;
        return count;
    }

    private String get_id(Map<String, Object> postion){
        return postion.get("text_id").toString();
    }

    private String get_name(Map<String, Object> postion){
        return postion.get("name").toString();
    }

    private Map<String, Object> from_entity_to_map(PositionEntity entity) {
        Map<String, Object> position = new HashMap<>();

        position.put("id", entity.getId());
        position.put("text_id", entity.getText_id());
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
