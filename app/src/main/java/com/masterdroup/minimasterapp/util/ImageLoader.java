package com.masterdroup.minimasterapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Description:图片控件统一调用 单例
 * Created by andmobi003 on 2016/7/12 16:00
 */
public class ImageLoader extends com.youth.banner.loader.ImageLoader {

    public static final String TAG = "IMageLoader";
    private static volatile ImageLoader instance;


    public static ImageLoader getInstance() {
        if (instance == null) {
            Class clazz = ImageLoader.class;
            synchronized (clazz) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }


    public void displayPicassoImage(String url, ImageView imageView) {

        DebugUtils.d(TAG, url);
        Picasso.with(imageView.getContext()).setIndicatorsEnabled(false);//For development you can enable the display of a colored ribbon which indicates the image source
        Picasso.with(imageView.getContext())//picasso默认情况下会使用全局的ApplicationContext，即开发者传进去Activity，picasso也会通过activity获取ApplicationContext。
                .load(url)
//               .centerCrop()//填补了ImageView的要求范围,然后修剪其余的范围。ImageView将被完全填满,但整个图像可能不会显示。
                .centerInside()//两个尺寸等于或小于请求的ImageView的界限。图像将显示完全,但可能不会填满整个ImageView。
                .fit()//fit()是测量目标ImageView和在内部使用的resize()来减少图像ImageView的尺寸。  关于fit()有两件事需要了解。首先,调用fit()会延迟图像请求因为picasso需要等到ImageView可以测量。第二,你只可以使用fit()和一个ImageView作为目标(稍后我们会看看其他目标)。
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//其中memoryPolicy的NO_CACHE是指图片加载时放弃在内存缓存中查找，NO_STORE是指图片加载完不缓存在内存中。
                .config(Bitmap.Config.RGB_565)//对于不透明的图片可以使用RGB_565来优化内存。
                .into(imageView);
    }


    public void displayGlideImage(String url, ImageView imageView, Context context, boolean isCircle) {

        if (isCircle) {
            Glide
                    .with(context)
                    .load(url)
                    .centerCrop()
                    .transform(new GlideCircleTransform(context))
                    .into(imageView);
        } else {
            Glide
                    .with(imageView.getContext())
                    .load(url)
                    .centerCrop()
                    .into(imageView);
        }
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide
                .with(context)
                .load(path)
                .centerCrop()
                .into(imageView);
    }


    /**
     * Created by qly on 2016/6/22.
     * 将图片转化为圆形
     */
    public class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }
}
