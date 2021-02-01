package com.project.hunsu.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReply is a Querydsl query type for Reply
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReply extends EntityPathBase<Reply> {

    private static final long serialVersionUID = 463729745L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReply reply = new QReply("reply");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> depth = createNumber("depth", Long.class);

    public final NumberPath<Long> groupNum = createNumber("groupNum", Long.class);

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final BooleanPath isActivated = createBoolean("isActivated");

    public final QOotd ootd;

    public final QUser user;

    public final QWear wear;

    public final DateTimePath<java.time.LocalDateTime> writeDate = createDateTime("writeDate", java.time.LocalDateTime.class);

    public QReply(String variable) {
        this(Reply.class, forVariable(variable), INITS);
    }

    public QReply(Path<? extends Reply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReply(PathMetadata metadata, PathInits inits) {
        this(Reply.class, metadata, inits);
    }

    public QReply(Class<? extends Reply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ootd = inits.isInitialized("ootd") ? new QOotd(forProperty("ootd"), inits.get("ootd")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
        this.wear = inits.isInitialized("wear") ? new QWear(forProperty("wear"), inits.get("wear")) : null;
    }

}

