package com.group6.swp391.services;

import com.group6.swp391.pojos.Thumnail;

import java.util.List;

public interface ThumnailService {
    Thumnail createThumnail(Thumnail thumnails);
    List<Thumnail> getAllThumnails();
    Thumnail getById(int id);
    void updateThumnail(int id, Thumnail thumnail);
    void deleteThumnail(int thumnailId);


    void updateThumnaiV2(Thumnail thumnail);
}
