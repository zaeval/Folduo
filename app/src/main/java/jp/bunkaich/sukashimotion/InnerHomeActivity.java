package jp.bunkaich.sukashimotion;

/**
 * Folduo home on the inner panel while dual display control is held, beside the user's own launcher.
 * That logical display has no system wallpaper, so a wallpaper-backed launcher appears on black there.
 * Its own task keeps it apart from HomeActivity when Folduo is also the default home.
 */
public final class InnerHomeActivity extends HomeActivity {}
