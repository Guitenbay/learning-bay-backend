package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.KElement;

import java.util.ArrayList;

public interface KElementService {
    ArrayList<KElement> getKElementList();
    KElement getKElementByUri(String uri);
    boolean isExist(String uri);
    boolean insert(KElement kElement);
    boolean update(KElement kElement);
    boolean delete(String uri);
}
