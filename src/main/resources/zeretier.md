### token
`I1kOkWOD8tefBxbEDz0DYcQHAo3NMhRG`

### bat
```angular2html
c:
cd "C:\Program Files (x86)\ZeroTier\One"
REM Join to private network
zerotier-cli join 56374ac9a4710c84
zerotier-cli set 56374ac9a4710c84 authorized=1
zerotier-cli set 56374ac9a4710c84 controller=zerotier
zerotier-cli set 56374ac9a4710c84 authtoken=I1kOkWOD8tefBxbEDz0DYcQHAo3NMhRG
REM Testing join
zerotier-cli status
zerotier-cli listnetworks
```