package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;

public final class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(),
                comment.getCreatedAt());
    }

    public static Comment toModel(CommentDto comment) {
        return new Comment(comment.getId(), comment.getText(), null, null, comment.getCreated());
    }
}
