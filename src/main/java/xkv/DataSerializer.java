package xkv;

import org.json.JSONException;
import org.json.JSONObject;
import xkv.content.Album;
import xkv.visual.panels.audion.Audion;

import java.io.*;
import java.util.logging.Level;

public class DataSerializer
{
    private static String directory = ResourceLoader.ResourceHeader.ALBUM_DATA.header();

    private static File directoryFile = new File(directory);

    private static void validate()
    {
        if (!directoryFile.exists() && !directoryFile.mkdir())
        {
            Firestarter.OUTPUT.log(Level.SEVERE, "Fatal serialization error: album data directory creation failed");
            System.exit(69);
        }
    }

    public static void serializeAlbums()
    {
        validate();

        Audion.forEachAlbum(album ->
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
        validate();

        File[] files = directoryFile.listFiles();

        if (files == null || files.length <= 0)
        {
            return;
        }

        for (File file : files)
        {
            if (file.exists())
            {
                try
                {
                    BufferedReader fileReader = new BufferedReader(new FileReader(file));
                    StringBuilder jsonBuilder = new StringBuilder();

                    fileReader.lines().forEachOrdered(jsonBuilder::append);

                    JSONObject albumJSON = new JSONObject(jsonBuilder.toString());
                    Album album = Album.deserialize(albumJSON);
                    Audion.addAlbum(album);
                }

                catch (IOException | JSONException e)
                {
                    Firestarter.OUTPUT.log(Level.WARNING, e.getLocalizedMessage());
                }
            }
        }
    }
}
