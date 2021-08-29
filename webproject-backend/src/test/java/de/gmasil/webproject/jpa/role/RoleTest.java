package de.gmasil.webproject.jpa.role;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;
import de.gmasil.webproject.jpa.GenericEntityTester;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserRepository;
import de.gmasil.webproject.utils.SetupTestContext;

@SetupTestContext
@Story("Testing database operation behaviour")
class RoleTest extends GenericEntityTester<Role, User> {

    @Autowired
    public RoleTest(RoleRepository roleRepo, UserRepository userRepo) {
        super("role", "user", roleRepo, userRepo);
    }

    @Transactional
    @Scenario("Persisting a role will persist attached users")
    void testPersistRoleAndAttachedUsers() {
        testPersistEntityAndAttachedEntities();
    }

    @Transactional
    @Scenario("When deleting a role it is detached from a user while preserving it")
    void testRoleDeletionPreservesUsers() {
        testEntityDeletionPreservesAttachedEntities();
    }

    @Override
    public Role createEntity() {
        return Role.builder().name("Role 1").build();
    }

    @Override
    public User createAttachedEntity() {
        return User.builder().username("User 1").password("pass1").build();
    }

    @Override
    public void attachToEntity(Role role, User user) {
        role.addUser(user);
    }

    @Override
    public void assertEntityHasAttachedEntity(Role role) {
        assertThat(role.getName(), is(equalTo("Role 1")));
        assertThat(role.getUsers(), hasSize(1));
        assertThat(role.getUsers().iterator().next().getUsername(), is(equalTo("User 1")));
    }

    @Override
    public void assertAttachedEntityHasEntityAttached(User user) {
        assertThat(user.getUsername(), is(equalTo("User 1")));
        assertThat(user.getRoles(), hasSize(1));
        assertThat(user.getRoles().iterator().next().getName(), is(equalTo("Role 1")));
    }

    @Override
    public void assertAttachedEntityHasNothingAttached(User user) {
        assertThat(user.getUsername(), is(equalTo("User 1")));
        assertThat(user.getRoles(), hasSize(0));
    }
}
