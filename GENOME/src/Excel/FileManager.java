package Excel;

import Data.*;
import Utils.Logs;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileManager {

    private static String s_RootFolderPath;
    private static String s_RootFolderName="Results";


    private  FileManager() throws IOException {
        s_RootFolderPath=System.getProperty("user.dir")+s_RootFolderName;
        CreatePath(s_RootFolderPath);
    }
    


      private static void CreatePath(String pathName) throws IOException {
        Path Path= Paths.get(pathName);

        if(Files.notExists(Path))
        {
            try {
                Files.createDirectories(Path);
            } catch (IOException e) {
                Logs.exception(e);
                throw  e;
            }

        }
    }

    /*
    * Get the path for the kingdom folder, create it if needed
     */
    public static String GetPathFor(Kingdom _kindgom) throws IOException{

        String KingdomPath= s_RootFolderPath+"/"+_kindgom.getName();

        CreatePath(KingdomPath);

        return  KingdomPath;
    }

    public static String GetPathFor(Group _group) throws IOException{


        String GroupPath= s_RootFolderPath+"/"+_group.getKingdomName()+"/"+_group.getName();

        CreatePath(GroupPath);

        return  GroupPath;
    }

    public static String GetPathFor(SubGroup _subGroup) throws IOException{


        String SubGroupPath= s_RootFolderPath+"/"+_subGroup.getKingdomName()+"/"+_subGroup.getGroupName()+"/"+_subGroup.getName();

        CreatePath(SubGroupPath);

        return  SubGroupPath;
    }



    public static String GetPathFor(Organism _organism)throws IOException{
        String OrganismPath= System.getProperty("user.dir")+"/"+s_RootFolderName+"/"+_organism.getKingdomName()+"/"+_organism.getGroupName()+"/"+_organism.getSubGroupName();

        CreatePath(OrganismPath);

        return  OrganismPath;
    }
}
