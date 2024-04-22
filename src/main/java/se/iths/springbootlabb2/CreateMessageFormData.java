package se.iths.springbootlabb2;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import se.iths.springbootlabb2.entities.MessageEntity;
import se.iths.springbootlabb2.entities.UserEntity;

@Getter
@Setter
public class CreateMessageFormData {

    @NotNull
    @Size(min = 1, max = 100)
    private String content;

    @NotNull
    private UserEntity userEntity;

    @NotNull
    private boolean isPublic;

    public CreateMessageFormData() {
    }

    public CreateMessageFormData(String content, UserEntity userEntity, boolean isPublic) {
        this.content = content;
        this.userEntity = userEntity;
        this.isPublic = isPublic;
    }

    public boolean getIsPublic() {
        return isPublic;
    }
    public MessageEntity toEntity() {
        var messageEntity = new MessageEntity();
        messageEntity.setPublic(isPublic);
        messageEntity.setContent(content);
        messageEntity.setUserEntity(userEntity);
        return messageEntity;
    }
}
