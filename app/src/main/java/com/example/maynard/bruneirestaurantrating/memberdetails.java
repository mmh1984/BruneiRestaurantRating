package com.example.maynard.bruneirestaurantrating;

/**
 * Created by Maynard on 4/6/2017.
 */

public class memberdetails {

    private String membername;
    private String memberemail;
    private String memberfacebook;
    private String memberphoto;

    public memberdetails(String membername, String memberemail, String memberfacebook, String memberphoto) {
        this.membername = membername;
        this.memberemail = memberemail;
        this.memberfacebook = memberfacebook;
        this.memberphoto = memberphoto;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMemberemail() {
        return memberemail;
    }

    public void setMemberemail(String memberemail) {
        this.memberemail = memberemail;
    }

    public String getMemberfacebook() {
        return memberfacebook;
    }

    public void setMemberfacebook(String memberfacebook) {
        this.memberfacebook = memberfacebook;
    }

    public String getMemberphoto() {
        return memberphoto;
    }

    public void setMemberphoto(String memberphoto) {
        this.memberphoto = memberphoto;
    }
}
