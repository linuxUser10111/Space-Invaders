package audioplayer;

import java.net.URL;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer implements AudioPlayerInterface {

	private static final String BACKGROUND_MUSIC_FILE = "BackgroundMusic.wav";
	private static final String SHOOTING_SOUND_FILE = "pew.mp3";
	private static final String EXPLODING_SOUND_FILE = "Explosion.wav";

	private static final double SHOOTING_SOUND_VOLUME = 0.8;
	private static final double EXPLODING_SOUND_VOLUME = 0.8;

	private final MediaPlayer musicPlayer;
	private final AudioClip crashPlayer;
	private final AudioClip explodPlayer;

	public AudioPlayer() {
		this.musicPlayer = new MediaPlayer(loadAudioFile(BACKGROUND_MUSIC_FILE));
		this.crashPlayer = new AudioClip(convertNameToUrl(SHOOTING_SOUND_FILE));
		this.explodPlayer = new AudioClip(convertNameToUrl(EXPLODING_SOUND_FILE));
	}

	@Override
	public void playBackgroundMusic() {
		if (isPlayingBackgroundMusic()) {
			return;
		}
		this.musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		this.musicPlayer.play();

	}

	@Override
	public boolean isPlayingBackgroundMusic() {
		return MediaPlayer.Status.PLAYING.equals(this.musicPlayer.getStatus());
	}

	@Override
	public void stopBackgroundMusic() {
		if (isPlayingBackgroundMusic()) {
			this.musicPlayer.stop();
		}
	}

	@Override
	public void playShootingSound() {
		crashPlayer.play(SHOOTING_SOUND_VOLUME);
	}

	@Override
	public void playExplosionSound() {
		explodPlayer.play(EXPLODING_SOUND_VOLUME);
	}

	private String convertNameToUrl(String fileName) {
		URL musicSourceUrl = getClass().getClassLoader().getResource(fileName);
		if (musicSourceUrl == null) {
			throw new IllegalArgumentException(
					"Please ensure that your resources folder contains the appropriate files for this exercise.");
		}
		return musicSourceUrl.toExternalForm();
	}

	private Media loadAudioFile(String fileName) {
		return new Media(convertNameToUrl(fileName));
	}
}