package com.bookstore.dao;

import com.bookstore.model.Role;
import com.bookstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.util.List;


@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //language=SQL
    private final String INSERT_USER = "insert into user_db(nickname,password,gender,firstname,middlename,lastname,birthday,enabled) values (?,?,?,?,?,?,?,?)";

    //language=SQL
    private final String SELECT_USER_BY_NICKNAME = "select * from user_db where nickname=?";

    //language=SQL
    private final String SELECT_USER_BY_ID = "select * from user_db where id=?";

    //language=SQL
    private final String SELECT_ROLE_BY_TITLE = "select * from role_db where title = ?";

    //language=SQL
    private final String SELECT_ROLE_BY_USER_NICKNAME = "select * from role_db " +
            "left join user_role_db on user_role_db.role_id = role_db.id " +
            "left join user_db on user_db.id = user_role_db.user_id " +
            "where user_db.nickname = ?";

    //language=SQL
    private final String INSERT_INTO_USER_ROLE_DB = "insert into user_role_db values (?,?)";


    //language=SQL
    private final String SELECT_USER_BY_SALE_ID = "select u_db.* from user_db u_db " +
            "left join sale_db s_db on s_db.user_id = u_db.id " +
            "where s_db.id = ?";


    //language=SQL
    private final String SELECT_USERS_BY_FIRSTNAME = "select * from user_db u_db " +
            "left join user_role_db u_r_db on u_r_db.user_id = id " +
            "left join role_db r_db on u_r_db.role_id = r_db.id " +
            "where lower(u_db.firstname) like ? or lower (u_db.firstname) like ? or lower(u_db.firstname) like ?";
    //language=SQL
    private final String SELECT_USERS_BY_MIDDLENAME = "select * from user_db u_db " +
            "left join user_role_db u_r_db on u_r_db.user_id = id " +
            "left join role_db r_db on u_r_db.role_id = r_db.id " +
            "where lower(u_db.middlename) like ? or lower (u_db.middlename) like ? or lower(u_db.middlename) like ?";
    //language=SQL
    private final String SELECT_USERS_BY_LASTNAME = "select * from user_db u_db " +
            "left join user_role_db u_r_db on u_r_db.user_id = id " +
            "left join role_db r_db on u_r_db.role_id = r_db.id " +
            "where lower(u_db.lastname) like ? or lower (u_db.lastname) like ? or lower(u_db.lastname) like ?";



    //language=SQL
    private final String SELECT_USERS_BY_FIRSTNAME_WITH_ROLE = "select * from user_db u_db " +
            "left join user_role_db u_r_db on u_r_db.user_id = id " +
            "left join role_db r_db on u_r_db.role_id = r_db.id " +
            "where (r_db.title=?) " +
            "and (lower(u_db.firstname) like ? or lower (u_db.firstname) like ? or lower(u_db.firstname) like ?)";
    //language=SQL
    private final String SELECT_USERS_BY_MIDDLENAME_WITH_ROLE = "select * from user_db u_db " +
            "left join user_role_db u_r_db on u_r_db.user_id = id " +
            "left join role_db r_db on u_r_db.role_id = r_db.id " +
            "where (r_db.title=?) " +
            "and (lower(u_db.middlename) like ? or lower (u_db.middlename) like ? or lower(u_db.middlename) like ?)";
    //language=SQL
    private final String SELECT_USERS_BY_LASTNAME_WITH_ROLE = "select * from user_db u_db " +
            "left join user_role_db u_r_db on u_r_db.user_id = id " +
            "left join role_db r_db on u_r_db.role_id = r_db.id " +
            "where (r_db.title=?) " +
            "and (lower(u_db.lastname) like ? or lower (u_db.lastname) like ? or lower(u_db.lastname) like ?)";

    //language=SQL
    private final String SELECT_USERS_SORT_BY_SOME_AZ = "select * from user_db order by ";

    //language=SQL
    private final String SELECT_USERS_SORT_BY_SOME_ZA = "select * from user_db order by ";

    //language=SQL
    private final String SELECT_SALES_BY_USER_ID = "select id from sale_db where user_id = ?";

    //language=SQL
    private final String DELETE_SALE_FROM_SALE_BOOK_DB = "delete from sale_book_db where sale_id = ?";

    //language=SQL
    private final String DELETE_SALE_FROM_SALE_DB_BY_USER_ID = "delete from sale_db where user_id = ?";

    //language=SQL
    private final String DELETE_USER_ROLE_BY_USER_ID = "delete from user_role_db where user_id = ?";

    //language=SQL
    private final String DELETE_USER_BY_ID = "delete from user_db where id = ?";

    //language=SQL
    private final String BAN_USER_BY_USER_ID = "update user_db set enabled = 'false' where id = ?";

    //language=SQL
    private final String UNBAN_USER_BY_USER_ID = "update user_db set enabled = 'true' where id = ?";

    //language=SQL
    private final String UPDATE_USER_COLUMN_BY_ID = "update user_db set ";

    //language=SQL
    private final String SELECT_ENABLED_FROM_USER_DB_BY_USER_ID = "select enabled from user_db where nickname = ?";

    private RowMapper<User> ROW_MAPPER_TO_USER = (ResultSet resultSet, int rawNum) -> {
        User user = User.builder()
                .userId(resultSet.getLong("id"))
                .nickname(resultSet.getString("nickname"))
                .password(resultSet.getString("password"))
                .birthday(resultSet.getDate("birthday"))
                .firstname(resultSet.getString("firstname"))
                .gender(resultSet.getString("gender"))
                .middlename(resultSet.getString("middlename"))
                .lastname(resultSet.getString("lastname"))
                .enabled(resultSet.getBoolean("enabled"))
                .build();

        Role role = findRoleByNickname(user.getNickname());
        user.setRole(role);
        return user;
    };

    private RowMapper<Role> ROW_MAPPER_TO_ROLE = (ResultSet resultSet, int rawNum) -> {
        return Role.builder()
                .title(resultSet.getString("title"))
                .id(resultSet.getLong("id"))
                .build();
    };

    private RowMapper<Long> ROW_MAPPER_TO_ID = (ResultSet resultSet, int rowNum) -> {
        return resultSet.getLong("id");
    } ;


    @Override
    public void createUser(User user) {
        jdbcTemplate.update(INSERT_USER, user.getNickname(), user.getPassword(), user.getGender(), user.getFirstname(), user.getMiddlename(), user.getLastname(), user.getBirthday(), user.isEnabled());
    }

    @Override
    public User findUserByNickname(String nickname) {
        List<User> user = jdbcTemplate.query(SELECT_USER_BY_NICKNAME, ROW_MAPPER_TO_USER, nickname);
        if (user.isEmpty()) {
            return null;
        } else {
            return user.get(0);
        }
    }

    @Override
    public User findUserById(Long id) {
        return jdbcTemplate.query(SELECT_USER_BY_ID, ROW_MAPPER_TO_USER, id).get(0);
    }

    @Override
    public User findUserBySaleId(Long saleId) {
        return jdbcTemplate.query(SELECT_USER_BY_SALE_ID,ROW_MAPPER_TO_USER,saleId).get(0);
    }

    @Override
    public List<User> findUsersByFirstName(String firstName) {
        String pre = '%' + firstName;
        String post =  firstName + '%';
        String prePost = '%' + firstName + '%';

        return jdbcTemplate.query(SELECT_USERS_BY_FIRSTNAME,ROW_MAPPER_TO_USER, pre,post,prePost);
    }

    @Override
    public List<User> findUsersByMiddleName(String middleName) {
        String pre = '%' + middleName;
        String post =  middleName + '%';
        String prePost = '%' + middleName + '%';
        return jdbcTemplate.query(SELECT_USERS_BY_MIDDLENAME,ROW_MAPPER_TO_USER, pre,post,prePost);
    }

    @Override
    public List<User> findUserByLastName(String lastName) {
        String pre = '%' + lastName;
        String post =  lastName + '%';
        String prePost = '%' + lastName + '%';
        return jdbcTemplate.query(SELECT_USERS_BY_LASTNAME,ROW_MAPPER_TO_USER, pre,post,prePost);
    }


    @Override
    public List<User> findUsersByFirstNameWithRole(String firstName, String roleTitle) {
        String firstNamePre = '%' + firstName;
        String firstNamePost =  firstName + '%';
        String firstNamePrePost = '%' + firstName + '%';

        return jdbcTemplate.query(SELECT_USERS_BY_FIRSTNAME_WITH_ROLE,ROW_MAPPER_TO_USER, roleTitle, firstNamePre,firstNamePost,firstNamePrePost);
    }


    @Override
    public List<User> findUsersByMiddleNameWithRole(String middleName, String roleTitle) {
        String firstNamePre = '%' + middleName;
        String firstNamePost =  middleName + '%';
        String firstNamePrePost = '%' + middleName + '%';

        return jdbcTemplate.query(SELECT_USERS_BY_MIDDLENAME_WITH_ROLE,ROW_MAPPER_TO_USER, roleTitle, firstNamePre,firstNamePost,firstNamePrePost);
    }



    @Override
    public List<User> findUserByLastNameWithRole(String lastName, String roleTitle) {
        String firstNamePre = '%' + lastName;
        String firstNamePost =  lastName + '%';
        String firstNamePrePost = '%' + lastName + '%';

        return jdbcTemplate.query(SELECT_USERS_BY_LASTNAME_WITH_ROLE,ROW_MAPPER_TO_USER,roleTitle, firstNamePre,firstNamePost,firstNamePrePost);
    }



    @Override
    public List<User> findAllUsersSortBySomeAZ(String some) {
        return jdbcTemplate.query(SELECT_USERS_SORT_BY_SOME_AZ + some, ROW_MAPPER_TO_USER);
    }

    @Override
    public List<User> findAllUsersSortBySomeZA(String some) {
        return jdbcTemplate.query(SELECT_USERS_SORT_BY_SOME_ZA + some + " desc", ROW_MAPPER_TO_USER);
    }

    @Override
    public void deleteUserById(Long id) {

        //Находим все id продаж, которые следует удалить
        List<Long> saleIds = jdbcTemplate.query(SELECT_SALES_BY_USER_ID,ROW_MAPPER_TO_ID,id);

        for (Long saleId : saleIds) {
            jdbcTemplate.update(DELETE_SALE_FROM_SALE_BOOK_DB,saleId);
        }

        jdbcTemplate.update(DELETE_SALE_FROM_SALE_DB_BY_USER_ID,id);

        jdbcTemplate.update(DELETE_USER_ROLE_BY_USER_ID,id);

        jdbcTemplate.update(DELETE_USER_BY_ID,id);
    }

    @Override
    public void banUserById(Long id) {
        jdbcTemplate.update(BAN_USER_BY_USER_ID,id);
    }

    @Override
    public void unbanUserById(Long id) {
        jdbcTemplate.update(UNBAN_USER_BY_USER_ID,id);
    }




    private RowMapper<Boolean> ROW_MAPPER_TO_BOOLEAN = (ResultSet resultSet, int rowNum) -> {
      return resultSet.getBoolean("enabled");
    };

    @Override
    public boolean isUserValidById(String nickname) {
        return jdbcTemplate.query(SELECT_ENABLED_FROM_USER_DB_BY_USER_ID,ROW_MAPPER_TO_BOOLEAN,nickname).get(0);
    }


    @Override
    public Role findRoleByNickname(String nickname) {

        List<Role> role = jdbcTemplate.query(SELECT_ROLE_BY_USER_NICKNAME, ROW_MAPPER_TO_ROLE, nickname);

        if (!role.isEmpty()) {
            return role.get(0);
        }
        return null;
    }

    @Override
    public void updateRoleByNickname(String titleRole, String nickname) {

        Long userId = 0L;
        Long roleId = 0L;

        List<User> userList = jdbcTemplate.query(SELECT_USER_BY_NICKNAME, ROW_MAPPER_TO_USER, nickname);

        if (!userList.isEmpty()) {

            userId = userList.get(0).getUserId();

            List<Role> roleList = jdbcTemplate.query(SELECT_ROLE_BY_TITLE, ROW_MAPPER_TO_ROLE, titleRole);

            if (!roleList.isEmpty()) {

                roleId = roleList.get(0).getId();

                jdbcTemplate.update(INSERT_INTO_USER_ROLE_DB, userId, roleId);
            }
        }
    }




    @Override
    public void updateUserColumnById(String titleColumn, String text, Long userId) {
        jdbcTemplate.update(UPDATE_USER_COLUMN_BY_ID + titleColumn + " = \'" + text + "\' where id = " + userId);
    }
}
