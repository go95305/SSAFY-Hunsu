package com.project.hunsu.model.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QVoteItemChoice is a Querydsl query type for VoteItemChoice
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVoteChoice extends EntityPathBase<VoteChoice> {

    private static final long serialVersionUID = 2039378620L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteChoice voteItemChoice = new QVoteChoice("voteItemChoice");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final QUser user;

    public final QVoteItem voteItem;

    public QVoteChoice(String variable) {
        this(VoteChoice.class, forVariable(variable), INITS);
    }

    public QVoteChoice(Path<? extends VoteChoice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoteChoice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoteChoice(PathMetadata metadata, PathInits inits) {
        this(VoteChoice.class, metadata, inits);
    }

    public QVoteChoice(Class<? extends VoteChoice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
        this.voteItem = inits.isInitialized("voteItem") ? new QVoteItem(forProperty("voteItem"), inits.get("voteItem")) : null;
    }

}

