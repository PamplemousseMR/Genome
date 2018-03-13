package Excel;

import Data.*;
import Utils.Logs;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public  class FileManager {

    private static String s_RootFolderPath;
    private static String s_RootFolderName="Results";


    private  FileManager() throws IOException {
       /* s_RootFolderPath=System.getProperty("user.dir")+s_RootFolderName;
        CreatePath(s_RootFolderPath);*/
    }
    


      private static String CreatePath(String pathName) throws IOException {
        pathName=System.getProperty("user.dir")+"/"+s_RootFolderName+"/"+pathName;

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

        return  pathName;
    }

    /*
    * Get the path for the kingdom folder, create it if needed
     */
    public static String GetPathFor(Kingdom _kindgom) throws IOException{

        String KingdomPath="";

        return  CreatePath(KingdomPath);
    }

    public static String GetPathFor(Group _group) throws IOException{


        String GroupPath= _group.getKingdomName();



        return   CreatePath(GroupPath);
    }

    public static String GetPathFor(SubGroup _subGroup) throws IOException{


        String SubGroupPath= _subGroup.getKingdomName()+"/"+_subGroup.getGroupName();



        return  CreatePath(SubGroupPath);
    }



    public static String GetPathFor(Organism _organism)throws IOException{
        String OrganismPath= _organism.getKingdomName()+"/"+_organism.getGroupName()+"/"+_organism.getSubGroupName();



        return   CreatePath(OrganismPath);
    }
}
