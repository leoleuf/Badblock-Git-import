package fr.badblock.common.autocompile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class AutoCompilerHttpHandler extends AbstractHandler {
	public static boolean activated = true;
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (target.length() > 1)
		{
			target = target.substring(1, target.length() - 1);
		}
		
		Main.doJob(target);
		response.setStatus(HttpServletResponse.SC_OK);
	}
}