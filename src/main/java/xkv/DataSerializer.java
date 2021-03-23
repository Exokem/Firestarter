package xkv;

import org.json.JSONObject;
import xkv.visual.panels.audion.AudionPanel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class DataSerializer
{
    public static void serializeAlbums()
    {
        String directory = ResourceLoader.ResourceHeader.ALBUM_DATA.header();

        File directoryFile = new File(directory);

        if (!directoryFile.exists() && !directoryFile.mkdir())
        {
            Firestarter.OUTPUT.log(Level.SEVERE, "Fatal serialization error: album data directory creation failed");
            System.exit(69);
        }

        AudionPanel.albums().forEach(album ->
        {
            JSONObject albumJSON = album.serialize();

            String path = album.displayName() + "_" + album.identifier() + ".json";
            File albumFile = new File(directory + path);

            try
            {
                if (!albumFile.exists() && !albumFile.createNewFile())
                {
                    Firestarter.OUTPUT.log(Level.WARNING, String.format("Serialization error: Data for album '%s' could not be saved", album.displayName()));
                }

                else
                {
                    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(albumFile));

                    fileWriter.write(albumJSON.toString());
                    fileWriter.close();
                }
            }

            catch (IOException e)
            {

            }



        });
    }

    public static void deserializeAlbums()
    {

    }
}
