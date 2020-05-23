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


    private RowMapper<User> ROW_MAPPER_TO_USER = (ResultSet resultSet, int rawNum) -> {
        User user = User.builder()
                .id(resultSet.getLong("id"))
                .nickname(resultSet.getString("nickname"))
                .password(resultSet.getString("password"))
                .birthday(resultSet.getDate("birthday"))
                .firstName(resultSet.getString("firstname"))
                .middleName(resultSet.getString("middlename"))
                .lastName(resultSet.getString("lastname"))
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


    @Override
    public void createUser(User user) {
        jdbcTemplate.update(INSERT_USER, user.getNickname(), user.getPassword(), user.getGender(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getBirthday(), user.isEnabled());
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
    public void deleteUserByNickname(String nickname) {

    }


    @Override
    public void deleteUserById(Long id) {

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

        //Надо найти ID пользователя с переданным именем
        List<User> userList = jdbcTemplate.query(SELECT_USER_BY_NICKNAME, ROW_MAPPER_TO_USER, nickname);

        if (!userList.isEmpty()) {

            userId = userList.get(0).getId();

            //Надо найти ID роли с переданным именем
            List<Role> roleList = jdbcTemplate.query(SELECT_ROLE_BY_TITLE, ROW_MAPPER_TO_ROLE, titleRole);

            if (!roleList.isEmpty()) {

                roleId = roleList.get(0).getId();

                jdbcTemplate.update(INSERT_INTO_USER_ROLE_DB, userId, roleId);
            }
        }
    }
}
