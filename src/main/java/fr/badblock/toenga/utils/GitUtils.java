package fr.badblock.toenga.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import fr.badblock.api.common.utils.FileUtils;
import fr.badblock.toenga.Toenga;
import fr.badblock.toenga.config.ToengaConfiguration;

public abstract class GitUtils
{
    public static void pullOrClone(ToengaConfiguration configuration, ToengaConfiguration.GitConfiguration.Repository repository)
    {
        boolean shouldClone = false;
        Git repos = null;
        final File folder = new File(Toenga.dataFolder, repository.getFolder());

        try
        {
            repos = Git.open(folder);
            final String url = repos.getRepository().getConfig().getString("remote", "origin", "url");
            if (!url.equals(repository.getUrl()))
            {
                shouldClone = true;
            }
        }
        catch (IOException e3)
        {
            shouldClone = true;
        }

        if (!shouldClone)
        {
            try
            {
                Toenga.log("Pull " + repository.getUrl() + " (" + repository.getBranch() + ")" + " in " + repository.getFolder() + "..");
                final PullCommand pull = repos.pull();
                pull.setProgressMonitor(new TextProgressMonitor());
                pull.setCredentialsProvider(new UsernamePasswordCredentialsProvider(configuration.getToengaData().getUser(), configuration.getToengaData().getPassword()));
                pull.call();
            }
            catch (GitAPIException e)
            {
                Toenga.logErr("Can't pull git repository", repository, e);
            }
            return;
        }

        if (folder.exists())
        {
            Toenga.log("Folder " + repository.getFolder() + " already exist, but we have to clone a repos. Deleting existing folder.");
            FileUtils.delete(folder);
        }

        Toenga.log("Clone " + repository.getUrl() + " (" + repository.getBranch() + ")" + " in " + repository.getFolder() + "..");

        final CloneCommand clone = Git.cloneRepository();
        clone.setDirectory(folder);
        clone.setURI(repository.getUrl());
        clone.setCloneSubmodules(true);
        clone.setBranchesToClone(Arrays.asList(repository.getBranch()));
        clone.setProgressMonitor(new TextProgressMonitor());
        clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(configuration.getToengaData().getUser(), configuration.getToengaData().getPassword()));

        try
        {
            clone.call();
        }
        catch (GitAPIException e)
        {
            Toenga.logErr("Can't clone git repository", repository, e);
        }
    }
}
