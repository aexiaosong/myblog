package com.ae.blog.service;

import com.ae.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TypeService {

    Type saveType(Type type);

    Optional<Type> getType(Long id);

    Type getTypeByName(String name);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    Type updateType(Long id,Type type);

    void deleteType(Long id);
}
