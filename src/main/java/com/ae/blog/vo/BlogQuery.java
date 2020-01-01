package com.ae.blog.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlogQuery {
    private String title;
    private Long typeId;
    private boolean recommend;
}
