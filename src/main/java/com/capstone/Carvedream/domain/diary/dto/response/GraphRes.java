package com.capstone.Carvedream.domain.diary.dto.response;

import com.capstone.Carvedream.domain.diary.domain.Emotion;
import lombok.Builder;
import lombok.Data;

@Data
public class GraphRes {

    private Emotion thrill;

    private Emotion yearning;

    private Emotion fear;

    private Emotion awkwardness;

    private Emotion mystery;

    private Emotion absurdity;

    private Emotion excited;

    private Emotion joy;

    private Emotion anger;

    @Builder
    public GraphRes(Emotion thrill, Emotion yearning, Emotion fear, Emotion awkwardness, Emotion mystery, Emotion absurdity, Emotion excited, Emotion joy, Emotion anger) {
        this.thrill = thrill;
        this.yearning = yearning;
        this.fear = fear;
        this.awkwardness = awkwardness;
        this.mystery = mystery;
        this.absurdity = absurdity;
        this.excited = excited;
        this.joy = joy;
        this.anger = anger;
    }
}
