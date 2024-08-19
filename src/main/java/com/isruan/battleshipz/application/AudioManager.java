package com.isruan.battleshipz.application;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {
	private boolean musicMuted = false;
	private boolean effectsMuted = false;
	private Clip musicClip;
	private Clip effectClip;

	public void playBgMusic(String filePath) {
		if (musicMuted) {
			return;
		}
		stopMusic(); // Stop any existing music before playing new
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
			if (musicClip != null && musicClip.isOpen()) {
				musicClip.close();
			}
			musicClip = AudioSystem.getClip();
			musicClip.open(audioStream);
			musicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop indefinitely
			musicClip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void setMusicMuted(boolean muted) {
		musicMuted = muted;
		if (musicClip != null) {
			if (muted) {
				musicClip.stop();
			} else {
				musicClip.start();
			}
		}
	}

	public void playSoundEffect(String filePath) {
		if (effectsMuted) {
			return;
		}
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
			if (effectClip != null && effectClip.isOpen()) {
				effectClip.close();
			}
			effectClip = AudioSystem.getClip();
			effectClip.open(audioStream);
			effectClip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void setEffectsMuted(boolean muted) {
		effectsMuted = muted;
		//		if (musicClip != null) {
		//			if (muted) {
		//				musicClip.stop();
		//			} else {
		//				musicClip.start();
		//			}
		//		}
	}

	private void stopMusic() {
		if (musicClip != null && musicClip.isRunning()) {
			musicClip.stop();
			musicClip.close();
		}
	}
}
