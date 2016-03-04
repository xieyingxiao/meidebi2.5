package com.meidebi.app.ui.picker;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;

import com.meidebi.app.XApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yazeed44 on 11/22/14.
 */
public final class AlbumUtil {


    public static int sLimit ;


    private AlbumUtil() {
        throw new AssertionError();
    }

    public static void initLimit(int limit) {
        sLimit = limit;
    }


    public static void loadAlbums(final RecyclerView gridView, final AlbumsFragment fragment) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {

                final ArrayList<AlbumEntry> albums = getAlbums(gridView.getContext(),false);
                setupAdapter(albums, gridView, fragment);

//            }
//        }).start();

    }
    public  static  final   String cameraFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + "Camera/";

    public static ArrayList<AlbumEntry> getAlbums(final Context context,Boolean refresh) {


        final String[] projectionPhotos = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                 MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.ORIENTATION
        };


        final ArrayList<AlbumEntry> albumsSorted = new ArrayList<AlbumEntry>();

        HashMap<Integer, AlbumEntry> albums = new HashMap<Integer, AlbumEntry>();
        AlbumEntry allPhotosAlbum = null;
        Integer cameraAlbumId = null;
        Cursor cursor = null;

        try {

            cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            if (cursor != null) {

                int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                final int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

                while (cursor.moveToNext()) {
                    int bucketId = cursor.getInt(bucketIdColumn);
                    String bucketName = cursor.getString(bucketNameColumn);


                    PhotoEntry photoEntry = createPhoto(cursor);

                    if (photoEntry.path == null || photoEntry.path.length() == 0) {
                        continue;
                    }


                    if (allPhotosAlbum == null) {
                        allPhotosAlbum = new AlbumEntry(0, "所有图片", photoEntry);
                        albumsSorted.add(0, allPhotosAlbum);
                    }
                    if (allPhotosAlbum != null) {
                        allPhotosAlbum.addPhoto(photoEntry);
                    }
                    AlbumEntry albumEntry = albums.get(bucketId);
                    if (albumEntry == null) {
                        albumEntry = new AlbumEntry(bucketId, bucketName, photoEntry);
                        albums.put(bucketId, albumEntry);
                        if (cameraAlbumId == null && cameraFolder != null && photoEntry.path != null && photoEntry.path.startsWith(cameraFolder)) {
                            albumsSorted.add(1, albumEntry);
                            cameraAlbumId = bucketId;
                        } else {
                            albumsSorted.add(albumEntry);
                        }
                    }
                    albumEntry.addPhoto(photoEntry);
                }


            }
        } catch (Exception ex) {
            Log.e("getAlbums", ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return albumsSorted;

    }

    private static PhotoEntry createPhoto(final Cursor cursor) {
        final int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        final int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
        final int orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
        final int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        final int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

        int imageId = cursor.getInt(imageIdColumn);
        int bucketId = cursor.getInt(bucketIdColumn);
        String path = cursor.getString(dataColumn);
        long dateTaken = cursor.getLong(dateColumn);
        int orientation = cursor.getInt(orientationColumn);

        return new PhotoEntry.Builder(path)
                .albumId(bucketId)
                .dateTaken(dateTaken)
                .orientation(orientation)
                .imageId(imageId)
                .build();

    }

    public static void setupAdapter(final ArrayList<AlbumEntry> albums, final RecyclerView gridView, final AlbumsFragment fragment) {


        final AlbumsAdapter adapter = new AlbumsAdapter(albums, fragment);
//         gridView.post(new Runnable() {
//            @Override
//            public void run() {
                 gridView.setAdapter(adapter);
//            }
//        });

    }


    public static class AlbumEntry implements Serializable {
        public final int Id;
        public final String name;
        public final PhotoEntry coverPhoto;
        public final ArrayList<PhotoEntry> photos = new ArrayList<PhotoEntry>();

        public AlbumEntry(int albumId, String albumName, PhotoEntry coverPhoto) {
            this.Id = albumId;
            this.name = albumName;
            this.coverPhoto = coverPhoto;
        }

        public void addPhoto(PhotoEntry photoEntry) {
            photos.add(photoEntry);
        }
    }

    public static class PhotoEntry implements Serializable {
        public final int albumId;
        public final int imageId;
        public final long dateTaken;
        public final String path;
        public final int orientation;

        public PhotoEntry(final Builder builder) {
            this.albumId = builder.mAlbumId;
            this.path = builder.mPath;
            this.orientation = builder.mOrientation;
            this.imageId = builder.mImageId;
            this.dateTaken = builder.mDateTaken;
        }


        public static class Builder {

            private final String mPath;
            private int mAlbumId;
            private int mImageId;
            private long mDateTaken;
            private int mOrientation;

            public Builder(final String path) {
                this.mPath = path;
            }

            public Builder albumId(int albumId) {
                this.mAlbumId = albumId;
                return this;
            }

            public Builder imageId(int imageId) {
                this.mImageId = imageId;
                return this;
            }

            public Builder dateTaken(final long dateTaken) {
                this.mDateTaken = dateTaken;
                return this;
            }

            public Builder orientation(final int orientation) {
                this.mOrientation = orientation;
                return this;
            }

            public PhotoEntry build() {
                return new PhotoEntry(this);
            }


        }

    }

    public static boolean isPicked(final AlbumUtil.PhotoEntry pPhotoEntry) {


        for (int i = 0; i < XApplication.getInstance().getSeletedPhoto().size(); i++) {
            final AlbumUtil.PhotoEntry photo = XApplication.getInstance().getSeletedPhoto().valueAt(i);

            if (photo.path.equals(pPhotoEntry.path)) {

                return true;
            }
        }

        return false;
    }


    public static ArrayList<PhotoEntry> asList(SparseArray<PhotoEntry> sparseArray) {
        if (sparseArray == null) return null;
        ArrayList<PhotoEntry> arrayList = new ArrayList<PhotoEntry>(sparseArray.size());
        for (int i = sparseArray.size()-1; i>0; i--) {
            arrayList.add(sparseArray.valueAt(i));
        }
         return arrayList;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
