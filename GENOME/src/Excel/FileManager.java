package Excel;

import Data.*;
import Utils.Logs;

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
     * Get the path for the data folder, create it if needed
     */
    public static String GetPathFor(DataBase _data) throws IOException{

        String Path="";

        Path =CreatePath(Path);

        return Path+"Total" + ".xlsx";
    }
    /*
    * Get the path for the kingdom folder, create it if needed
     */
    public static String GetPathFor(Kingdom _kindgom) throws IOException{

        String KingdomPath="";

        KingdomPath=  CreatePath(KingdomPath);

        return  KingdomPath+"Total_"+ _kindgom.getName() + ".xlsx";
    }

    public static String GetPathFor(Group _group) throws IOException{


        String GroupPath= _group.getKingdomName();

        GroupPath =  CreatePath(GroupPath);

        return  GroupPath + "/Total_" + _group.getName() + ".xlsx";
    }

    public static String GetPathFor(SubGroup _subGroup) throws IOException{


        String SubGroupPath= _subGroup.getKingdomName()+"/"+_subGroup.getGroupName();

        SubGroupPath =  CreatePath(SubGroupPath);

        return SubGroupPath+ "/Total_" +  _subGroup.getName() + ".xlsx";
    }



    public static String GetPathFor(Organism _organism)throws IOException{
        String OrganismPath= _organism.getKingdomName()+"/"+_organism.getGroupName()+"/"+_organism.getSubGroupName();

        OrganismPath =   CreatePath(OrganismPath);

        return  OrganismPath+ "/" +  _organism.getName() + ".xlsx";
    }
}
