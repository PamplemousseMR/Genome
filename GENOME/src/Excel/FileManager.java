package Excel;

import Data.Kingdom;
import Data.Organism;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileManager {

    private static String s_RootFolderPath;
    private static String s_RootFolderName="Results";

    private  FileManager(){
        s_RootFolderPath=System.getProperty("user.dir")+s_RootFolderName;
    }



    /*
    * Get the path for the kingdom folder, create it if need
     */
    public static String GetPathFor(Kingdom _kindgom){


        String KingdomPath= s_RootFolderPath+"/"+_kindgom.getName();
        Path Path= Paths.get(KingdomPath);

        if(Files.notExists(Path))
        {

        }

        return  "oui";
    }

    private  void CreatePathFor(Organism _organism)
    {

    }

    public static String GetPathFor(Organism _organism){
        /*String Root= System.getProperty("user.dir")+s_RootFolder;

        String Path=Root;






        return  Path+"/"+_organism.getName()+".xlsx";*/
        return  "oui";
    }
}
