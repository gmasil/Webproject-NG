/*
 * Webproject NG
 * Copyright © 2021 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Webproject NG is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.
 */
package de.gmasil.webproject.controller.rest;

//@RestController
//@RequestMapping("/api/users")
public class UserRestController {
    /*-
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private UserService userService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PermitAll
    @GetMapping("/current")
    public ResponseEntity<Object> currentUser() {
        User user = userProvider.getCurrent();
        if (user != null) {
            Optional<UserProjectionNEW> eagerUser = userRepo.findProjectionById(user.getId(), UserProjectionNEW.class);
            if (eagerUser.isPresent()) {
                return ResponseEntity.ok(eagerUser.get());
            }
        }
        return ResponseEntity.ok("null");
    }
    
    @Transactional
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updatepassword")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UserPasswordDto userPassword) {
        User user = userProvider.getCurrent();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        user = entityManager.merge(user);
        if (!userService.verifyPassword(userPassword.getCurrentPassword(), user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password does not match");
        }
        user.setPassword(userPassword.getNewPassword());
        userService.encodePassword(user);
        userService.save(user);
        return ResponseEntity.ok().build();
    }
    */
}
