package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.MediaMaterial;

import java.util.ArrayList;

public interface MediaMaterialService {
    ArrayList<MediaMaterial> getMediaMaterialList();
    MediaMaterial getMediaMaterialByUri(String uri);
    boolean isExist(String uri);
    boolean insert(MediaMaterial mediaMaterial);
    boolean update(MediaMaterial mediaMaterial);
    boolean delete(String uri);
}
