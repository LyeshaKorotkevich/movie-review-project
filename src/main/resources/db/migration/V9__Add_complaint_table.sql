CREATE TABLE Complaint
(
    id         UUID                        NOT NULL,
    reason     VARCHAR(255)                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id    UUID                        NOT NULL,
    review_id  UUID                        NOT NULL,
    status     VARCHAR(255),
    CONSTRAINT pk_complaint PRIMARY KEY (id)
);

ALTER TABLE Complaint
    ADD CONSTRAINT FK_COMPLAINT_ON_REVIEW FOREIGN KEY (review_id) REFERENCES Review (id);

ALTER TABLE Complaint
    ADD CONSTRAINT FK_COMPLAINT_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);