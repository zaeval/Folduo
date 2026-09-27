package jp.bunkaich.sukashimotion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.*;

/**
 * Plain background kept directly under the default launcher on the inner panel while dual display
 * control is held. That logical display has no system wallpaper, and One UI Home draws translucently
 * over whatever lies beneath it, so without this it shows black there (seen on SM-F966N).
 */
public final class InnerBackdropActivity extends Activity {
    private final Handler main=new Handler(Looper.getMainLooper());
    private HomeScene.Wallpaper wallpaper;private long stamp=-1;
    // Anything under the launcher shows through it, so never linger on the cover (display 0). The helper
    // starts this there and moves it to the inner display within a few hundred milliseconds.
    private final Runnable leaveCover=()->{if(!isFinishing()&&getDisplay()!=null&&getDisplay().getDisplayId()==0)finishAndRemoveTask();};
    @Override protected void onCreate(Bundle state){
        super.onCreate(state);
        getWindow().setDecorFitsSystemWindows(false);
        wallpaper=new HomeScene.Wallpaper(this);wallpaper.inner=true;setContentView(wallpaper);
        getOnBackInvokedDispatcher().registerOnBackInvokedCallback(android.window.OnBackInvokedDispatcher.PRIORITY_DEFAULT,()->{});
    }
    @Override protected void onStart(){
        super.onStart();
        long next=HomeBackground.stamp(this);
        if(next!=stamp){stamp=next;new Thread(()->{Bitmap photo=HomeBackground.load(this);main.post(()->{if(!isDestroyed()){wallpaper.photo=photo;wallpaper.invalidate();}});},"inner-backdrop").start();}
    }
    @Override protected void onResume(){super.onResume();checkDisplay();}
    @Override public void onConfigurationChanged(android.content.res.Configuration config){super.onConfigurationChanged(config);checkDisplay();}
    private void checkDisplay(){main.removeCallbacks(leaveCover);main.postDelayed(leaveCover,1500);}
    @Override protected void onDestroy(){main.removeCallbacks(leaveCover);super.onDestroy();}
}
