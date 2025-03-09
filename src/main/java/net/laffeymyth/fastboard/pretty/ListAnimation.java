package net.laffeymyth.fastboard.pretty;

import lombok.Getter;

import java.util.List;

@Getter
public class ListAnimation<T> extends BaseAnimation<T> {
    public ListAnimation(List<T> displays) {
        super(displays);
    }
}