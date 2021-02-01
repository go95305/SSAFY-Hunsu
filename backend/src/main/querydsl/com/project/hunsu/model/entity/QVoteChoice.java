package com.project.hunsu.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoteChoice is a Querydsl query type for VoteChoice
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVoteChoice extends EntityPathBase<VoteChoice> {

    private static final long serialVersionUID = 230698020L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteChoice voteChoice = new QVoteChoice("voteChoice");

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

