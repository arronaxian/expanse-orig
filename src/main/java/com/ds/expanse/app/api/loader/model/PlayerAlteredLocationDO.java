package com.ds.expanse.app.api.loader.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Maintains any modifications to the player's location.  This information is for book keeping purposes only.
 * Records only exist when there is a change.
 *
 * The key is player.id and location.id.
 */
@Document(collection = "playeralteredlocation")
public class PlayerAlteredLocationDO extends LocationDO {
    public PlayerAlteredLocationDO() {
        super();
    }

    @DBRef @Setter @Getter PlayerDO player;
    @DBRef @Setter @Getter LocationDO location;
}
