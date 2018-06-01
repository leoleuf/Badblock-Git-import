package fr.badblock.toenga.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ToengaConfiguration
{
	private String								toengaSocket;
    private GitConfiguration					toengaData;
    
    public ToengaConfiguration()
    {
        setToengaSocket("toenga.sock");
        setToengaData(new GitConfiguration());
    }
    
    @Data
    public class GitConfiguration
    {
    	
        private String			user;
        private String			password;
        private Repository[]	repositories;
        private Locations		locations;
        
        public GitConfiguration()
        {
            setUser("example");
            setPassword("password");
            setRepositories(new Repository[] { new Repository() });
            setLocations(new Locations());
        }

        @Data
        public class Repository
        {
        	
            private String	url;
            private String	folder;
            private String	branch;
            
            public Repository()
            {
                setUrl("http://example.com/example.git");
                setFolder("example");
                setBranch("master");
            }
            
            @Override
            public String toString()
            {
                return "ToengaConfiguration.GitConfiguration.Repository(url=" + getUrl() + ", folder=" + getFolder() + ", branch=" + getBranch() + ")";
            }
            
        }
        
        @Data
        public class Locations
        {
        	
            private String[]		models;
            private String			toengaConfig;
            private String			toengaJar;
            private String			toengaProcessJar;
            
            public Locations()
            {
                setModels(new String[] {"models"});
                setToengaConfig("toenga/config.json");
                setToengaJar("toenga/Toenga.jar");
                setToengaProcessJar("toenga/ToengaProcess.jar");
            }            
        }
        
    }
    
}