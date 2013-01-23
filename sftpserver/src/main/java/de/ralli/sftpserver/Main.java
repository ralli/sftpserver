package de.ralli.sftpserver;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.UserAuth;
import org.apache.sshd.server.auth.UserAuthPublicKey;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ralli.sftpserver.core.util.impl.KeySerializerImpl;
import de.ralli.sftpserver.filesystem.SimpleFileSystemFactory;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public class BogusPublickeyAuthenticator implements PublickeyAuthenticator {

		public boolean authenticate(String username, PublicKey key,
				ServerSession session) {
		    KeySerializerImpl keySerializer = new KeySerializerImpl();
		    log.info("fingerprint: {}", keySerializer.getFingerprintString(key));
			return true;
		}
	}

	public class SimplePasswordAuthenticator implements PasswordAuthenticator {
		@Override
		public boolean authenticate(String username, String password,
				ServerSession session) {
			return "hase".equals(password);
		}

	}

	private void run() throws Exception {
		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(6322);
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(
				"hostkey.ser"));

		List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
		userAuthFactories.add(new UserAuthPublicKey.Factory());
		sshd.setUserAuthFactories(userAuthFactories);
		sshd.setPublickeyAuthenticator(new BogusPublickeyAuthenticator());
		sshd.setPasswordAuthenticator(new SimplePasswordAuthenticator());
		sshd.setCommandFactory(new ScpCommandFactory());
		sshd.setFileSystemFactory(new SimpleFileSystemFactory("/home/ralli/test"));
		
		List<NamedFactory<Command>> namedFactoryList = new ArrayList<NamedFactory<Command>>();
		namedFactoryList.add(new SftpSubsystem.Factory());
		sshd.setSubsystemFactories(namedFactoryList);

		sshd.start();
	}

	public static final void main(String args[]) {
		try {
			new Main().run();
		} catch (Exception ex) {
			log.error("Fehler", ex);
		}
	}
}
