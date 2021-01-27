package com.project.hunsu.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoteItem is a Querydsl query type for VoteItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVoteItem extends EntityPathBase<VoteItem> {

    private static final long serialVersionUID = -739163845L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteItem voteItem = new QVoteItem("voteItem");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final StringPath item = createString("item");

    public final QVote vote;

    public QVoteItem(String variable) {
        this(VoteItem.class, forVariable(variable), INITS);
    }

    public QVoteItem(Path<? extends VoteItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoteItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoteItem(PathMetadata metadata, PathInits inits) {
        this(VoteItem.class, metadata, inits);
    }

    public QVoteItem(Class<? extends VoteItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.vote = inits.isInitialized("vote") ? new QVote(forProperty("vote"), inits.get("vote")) : null;
    }

}

