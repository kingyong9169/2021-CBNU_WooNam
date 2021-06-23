package com.app_services.WooNam.chattingapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app_services.WooNam.chattingapp.UserModel.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {//액티비티를 상속해줄 profileActivity를 생성
    EditText name, username, email,school,git,aword,favorite;
    EditText about;
    CircleImageView profile_pic;
    LinearLayout ll_for_name, ll_for_about, ll_for_username,ll_for_school,ll_for_favorite,ll_for_aword,ll_for_git;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    StorageReference storageReference;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//홈 버튼으로 나갔다 들어왓을 경우 앱을 죽지 않게 처리를 할 수 있도록 도와줌
        super.onCreate(savedInstanceState);//오버라이드된 메소드를처리한다 .
        setContentView(R.layout.activity_profile);//profile을 보여줌

        Toolbar toolbar = findViewById(R.id.toolbar);//main.xml에서 사용하는 Toolbar를 변경할수 있는 메소드를 지원
        setSupportActionBar(toolbar);//toolbar의 액션바 설정
        getSupportActionBar().setTitle("프로필");//tilte을 보여줌
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기 버튼
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//뒤로가기 버튼 누를시 toolbar에 표현
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//oncreat에서 사용되는것들 정의
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        about = findViewById(R.id.user_about);
        email = findViewById(R.id.email);
        school= findViewById(R.id.school);
        profile_pic = findViewById(R.id.user_profile_pic);
        git= findViewById(R.id.git);
        favorite= findViewById(R.id.favorite);
        aword= findViewById(R.id.aword);

        ll_for_name = findViewById(R.id.ll_for_name);
        ll_for_username = findViewById(R.id.ll_for_user_name);
        ll_for_about = findViewById(R.id.ll_for_about);
        ll_for_school = findViewById(R.id.ll_for_school);
        ll_for_git = findViewById(R.id.ll_for_git);
        ll_for_favorite = findViewById(R.id.ll_for_favorite);
        ll_for_aword= findViewById(R.id.ll_for_awoard);



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();//Firebase에서 사용자 관리
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());//firebase연결

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//경로의 전체 내용을 읽고 변경사항을 수신 대기

                User user = dataSnapshot.getValue(User.class);//user의 정보를 DataSnapshot으로 받음
                Log.i("DATA SNAPSHOT ", "onDataChange: data = "+ dataSnapshot.getValue().toString());
                String fullName = user.getName();
                name.setText(fullName);
                username.setText(user.getUsername());
                school.setText(user.getSchool());
                about.setText(user.getUser_about());
                email.setText(firebaseUser.getEmail());
                git.setText(user.getGit());
                favorite.setText(user.getFavorite());
                aword.setText(user.getAword());



                if (user.getImageURL().equals("default")){//사용자프로필의 이미지 설정이 되어있지않다면
                    profile_pic.setImageResource(R.mipmap.ic_default_profile_pic);//default이미지로 설정함
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_pic);//설정했으면 설정한 이미지보여줌
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {//데이터베이스가 에러나면 취소이벤트

            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {//profile_pic 클릭이벤트
            @Override
            public void onClick(View v) {
                openImage();
            }//이미지열기클릭시 이미지 보여줌
        });

        ll_for_name.setOnClickListener(new View.OnClickListener() {//사용자의 이름을 클릭하면
            @Override
            public void onClick(View v) {// 보여준다
                final String oldName = name.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Enter Name");


                final EditText input = new EditText(getApplicationContext());
                input.setText(name.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//ok 클릭시 dialog의 위치를 설정해줌
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//dialog 클릭이벤트
                        name.setText(input.getText().toString());
                        HashMap<String, Object> hashMap = new HashMap<>();//string과 object를 한번에 저장
                        hashMap.put("name", name.getText().toString());
                        reference.updateChildren(hashMap);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {// cancel 버튼과 버튼을 눌렀을 경우의 처리
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name.setText(oldName);
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        name.setOnClickListener(new View.OnClickListener() {//name의 클릭이벤트처리
            @Override
            public void onClick(View v) {
                ll_for_name.performClick();
            }
        });

        ll_for_about.setOnClickListener(new View.OnClickListener() {//자기소개의 클릭이벤트 처리
            @Override
            public void onClick(View v) {
                final String oldAbout = about.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Enter About");


                final EditText input = new EditText(getApplicationContext());
                input.setText(about.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//ok 클릭시 dialog의 위치를 설정해줌
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        about.setText(input.getText().toString());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("user_about", about.getText().toString());
                        reference.updateChildren(hashMap);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {// cancel 버튼과 버튼을 눌렀을 경우의 처리
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        about.setText(oldAbout);
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {//자기소개의 이벤트 클릭처리
            @Override
            public void onClick(View v) {
                ll_for_about.performClick();
            }
        });


        ll_for_username.setOnClickListener(new View.OnClickListener() {//사용자 이름의 이벤트 클릭처리
            @Override
            public void onClick(View v) {
                final String oldUserName = username.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Enter Username");


                final EditText input = new EditText(getApplicationContext());
                input.setText(username.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//ok 클릭시 dialog의 위치를 설정해줌
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        username.setText(input.getText().toString());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("username", username.getText().toString());
                        reference.updateChildren(hashMap);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {// cancel 버튼과 버튼을 눌렀을 경우의 처리
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        username.setText(oldUserName);
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


        username.setOnClickListener(new View.OnClickListener() {//사용자 이름의 클릭이벤트 추가
            @Override
            public void onClick(View v) {
                ll_for_username.performClick();
            }
        });


        ll_for_favorite.setOnClickListener(new View.OnClickListener() {//관심분야의 클릭이벤트 추가
            @Override
            public void onClick(View v) {
                final String oldFavorite = favorite.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Enter favorite");


                final EditText input = new EditText(getApplicationContext());
                input.setText(favorite.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//ok 클릭시 dialog의 위치를 설정해줌
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favorite.setText(input.getText().toString());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("favorite", favorite.getText().toString());
                        reference.updateChildren(hashMap);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {// cancel 버튼과 버튼을 눌렀을 경우의 처리
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favorite.setText(oldFavorite);
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


        favorite.setOnClickListener(new View.OnClickListener() {//관심분야의 클릭이벤트처리
            @Override
            public void onClick(View v) {
                ll_for_favorite.performClick();
            }
        });

        ll_for_git.setOnClickListener(new View.OnClickListener() {//깃주소의 클릭이벤트 처리
            @Override
            public void onClick(View v) {
                final String oldGit = git.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Enter git");


                final EditText input = new EditText(getApplicationContext());
                input.setText(git.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//ok 클릭시 dialog의 위치를 설정해줌
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        git.setText(input.getText().toString());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("git", git.getText().toString());
                        reference.updateChildren(hashMap);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {// cancel 버튼과 버튼을 눌렀을 경우의 처리
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        git.setText(oldGit);
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


        git.setOnClickListener(new View.OnClickListener() {//깃의이벤트클릭처리
            @Override
            public void onClick(View v) {
                ll_for_git.performClick();
            }
        });

        ll_for_aword.setOnClickListener(new View.OnClickListener() {//수상내역의 이벤트 클릭처리
            @Override
            public void onClick(View v) {
                final String oldAword = aword.getText().toString();//두개가 일치한다면
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);//클래스가 맞춤 레이아웃을 포함하여 이와 같은 종류의 콘텐츠가 있는 AlertDialog 를 생성할 수 있는 API를 제공
                builder.setTitle("Enter aword");


                final EditText input = new EditText(getApplicationContext());
                input.setText(aword.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//ok 클릭시 dialog의 위치를 설정해줌
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aword.setText(input.getText().toString());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("aword", aword.getText().toString());
                        reference.updateChildren(hashMap);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {// cancel 버튼과 버튼을 눌렀을 경우의 처리
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aword.setText(oldAword);
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


        aword.setOnClickListener(new View.OnClickListener() {//aword의 클릭 이벤트 관리
            @Override
            public void onClick(View v) {
                ll_for_aword.performClick();
            }
        });

        ll_for_school.setOnClickListener(new View.OnClickListener() {  //학교 클릭 이벤트 관리
            @Override
            public void onClick(View v) {
                final String oldSchool = school.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Enter School");


                final EditText input = new EditText(getApplicationContext());
                input.setText(school.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//ok 클릭시 dialog의 위치를 설정해줌
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        school.setText(input.getText().toString());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("school", school.getText().toString());
                        reference.updateChildren(hashMap);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {// cancel 버튼과 버튼을 눌렀을 경우의 처리
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        school.setText(oldSchool);
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        school.setOnClickListener(new View.OnClickListener() {//school의 클릭 이벤트 관리
            @Override
            public void onClick(View v) {
                ll_for_school.performClick();
            }
        });
    }


    private void openImage(){//이미지를 보여줌
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);

    }

    private String getFileExtension(Uri uri){//업로드 된 파일의 확장자를 반환

        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        assert mimeType != null;//mimetype가 아니면 앱종료
        return mimeType.split("/")[1];

    }

    private void uploadImage(){//이미지를 업로드
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading");
        dialog.show();

        if (imageUri != null){
            Log.i("Image", "uploadImage: URL " + imageUri.toString());
            Log.i("Image", "uploadImage: URL " + getFileExtension(imageUri));

            String image_url = System.currentTimeMillis()+ "." + getFileExtension(imageUri);

            Log.i("Image", "uploadImage: URL = " + image_url);

            final StorageReference fileReference = storageReference.child(image_url);//image_url을 가져오기위해 파일저장소를 참조
            uploadTask = fileReference.putFile(imageUri);//업로드task를진행함

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){//이미지가 성공적으로 업로드 되지못했으면
                        throw task.getException();//예외발생던져줌
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {//이미지가 성공적으로 업로되 되었으면 성공했다는 이벤트
                @Override
                public void onComplete(@NonNull Task<Uri> task) {//이미지 성공시 실행되는 task들
                    Log.i("task.isSuccessful()", "onComplete: task.isSuccessful() = " + task.isSuccessful());
                    if (task.isSuccessful()) {
                        Log.i("downloadUri", "onComplete: taskResult = " + task.getResult().toString());
                        Uri downloadUri = task.getResult();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL", task.getResult().toString());
                        Toast.makeText(ProfileActivity.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        reference.updateChildren(hashMap);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Uploading Failed!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {//이미지 선택이 실패할시
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

        } else {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//main액티비티에서 sub액티비티를 호출하여 넘어갔다가, 다시 main 액티비티로 돌아올때 사용
        super.onActivityResult(requestCode, resultCode, data);//main액티비티에서 sub액티비티를 호출하여 넘어갔다가, 다시 main 액티비티로 돌아올때 사용

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(this, "Uploading Image!", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    private void Status(){//상태출력
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", "online");//온라인일시 온라인 으로 표시 (초록색)
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {//생명주기를 관리함, 유저와 상호작용이 가능하고 onstart일때,화면이 노출되나 상호작용이 불가능
        super.onResume();
        Status();
    }
}