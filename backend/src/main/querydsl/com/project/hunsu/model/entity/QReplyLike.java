package com.project.hunsu.model.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QReplyLike is a Querydsl query type for ReplyLike
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReplyLike extends EntityPathBase<ReplyLike> {

    private static final long serialVersionUID = -2059095741L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReplyLike replyLike = new QReplyLike("replyLike");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final QReply reply;

    public final QUser user;

    public QReplyLike(String variable) {
        this(ReplyLike.class, forVariable(variable), INITS);
    }

    public QReplyLike(Path<? extends ReplyLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReplyLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReplyLike(PathMetadata metadata, PathInits inits) {
        this(ReplyLike.class, metadata, inits);
    }

    public QReplyLike(Class<? extends ReplyLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reply = inits.isInitialized("reply") ? new QReply(forProperty("reply"), inits.get("reply")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

