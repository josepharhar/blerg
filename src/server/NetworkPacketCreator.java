package server;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import common.Entity;
import common.networking.UpdateFooter;

public class NetworkPacketCreator {
    private Game game;
    private volatile String latestEntityTransmission = "";
    private ObjectWriter entityWriter;
    private ObjectWriter footerWriter;
    
    public NetworkPacketCreator(ObjectMapper mapper, Game game, ScheduledExecutorService executor) {
        this.game = game;
        this.entityWriter = mapper.writerWithType(Entity.class);
        this.footerWriter = mapper.writerWithType(UpdateFooter.class);
        executor.scheduleAtFixedRate(() -> updateEntityTransmissionString(), 100, 100, TimeUnit.MILLISECONDS);
    }
    
    public String getPacket(UpdateFooter header) {
        String footerString;
        try {
            footerString = footerWriter.writeValueAsString(header);
        } catch (Exception e) {
            System.out.println("Exception caught in generation of transmission string");
            throw new RuntimeException(e);
        }
        return latestEntityTransmission + footerString;
    }
    
    private void updateEntityTransmissionString() {
        List<Entity> entities = game.getEntities();
        StringBuilder sb = new StringBuilder();
        for (Entity entity : entities) {
            if (!entity.hasBeenEaten()) {
                try {
                    sb.append(entityWriter.writeValueAsString(entity));
                } catch (Exception e) {
                    System.out.println("Exception caught in generation of transmission string");
                    throw new RuntimeException(e);
                }
                sb.append("\0");
            }
        }
        latestEntityTransmission = sb.toString();
    }
}
