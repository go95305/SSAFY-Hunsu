package com.project.hunsu.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoteItemChoice is a Querydsl query type for VoteItemChoice
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVoteItemChoice extends EntityPathBase<VoteItemChoice> {

    private static final long serialVersionUID = 2039378620L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteItemChoice voteItemChoice = new QVoteItemChoice("voteItemChoice");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final QUser user;

    public final QVoteItem voteItem;

    public QVoteItemChoice(String variable) {
        this(VoteItemChoice.class, forVariable(variable), INITS);
    }

    public QVoteItemChoice(Path<? extends VoteItemChoice> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoteItemChoice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoteItemChoice(PathMetadata metadata, PathInits inits) {
        this(VoteItemChoice.class, metadata, inits);
    }

    public QVoteItemChoice(Class<? extends VoteItemChoice> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
        this.voteItem = inits.isInitialized("voteItem") ? new QVoteItem(forProperty("voteItem"), inits.get("voteItem")) : null;
    }

}

